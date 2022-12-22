import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.Frame
import org.bytedeco.javacv.Java2DFrameConverter
import java.net.URL
import java.nio.ByteBuffer
import java.nio.ShortBuffer
import javax.sound.sampled.*
import kotlin.time.Duration.Companion.microseconds
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun VideoPlayer(url: URL, width: Int, height: Int) {
    val player = remember { VideoPlayerClass(url, width , height) }
    val audioTimestamp by player.audioTimestamp.collectAsState()
    val soundLineTimestamp by player.soundLineTimestamp.collectAsState()
    val videoTimestamp by player.videoTimestamp.collectAsState()
    val frame by player.videoFrames.collectAsState()
    remember { player.start() }
    Column {
        Canvas(Modifier.width(width.dp).height(height.dp)) {
            frame?.let(::drawImage)
        }
        Text("Video timestamp:         $videoTimestamp")
        Text("Audio timestamp:         $audioTimestamp")
        Text("SoundLine timestamp: $soundLineTimestamp")
    }
}

/**
 * NOTE: We could also probably use https://github.com/mahozad/jlayer to play audio/sound.
 */
class VideoPlayerClass(
    sourcePath: URL,
    width: Int,
    height: Int
) {
    private val scope = CoroutineScope(Dispatchers.IO)
    private val imageConverter = Java2DFrameConverter()
    private val videoGrabber = FFmpegFrameGrabber(sourcePath)
    private val audioGrabber = FFmpegFrameGrabber(sourcePath)
    private var soundLine: SourceDataLine? = null
    private val _videoFrames = MutableStateFlow<ImageBitmap?>(null)
    private val _videoTimestamp = MutableStateFlow<Long>(0)
    private val _audioTimestamp = MutableStateFlow<Long>(0)
    private val _soundLineTimestamp = MutableStateFlow<Long>(0)
    private var isResumed = true
    val videoFrames = _videoFrames.asStateFlow()
    val videoTimestamp = _videoTimestamp.asStateFlow()
    val audioTimestamp = _audioTimestamp.asStateFlow()
    val soundLineTimestamp = _soundLineTimestamp.asStateFlow()

    init {
        videoGrabber.imageWidth = width // Comment so it defaults to video original width
        videoGrabber.imageHeight = height // Comment so it defaults to video original height
        // grabber.frameRate = 123.4 // Not required
    }

    /**
     * We sync the video to audio.
     * -sync type
     * From [ffplay documentation](https://ffmpeg.org/ffplay.html):
     * ... Most media players use audio as master clock, but in some cases (streaming or high quality broadcast)
     * it is necessary to change that.
     *
     * Played 30 minutes of video on file://.... in sync successfully (I closed the app after 30 minutes).
     *
     * NOTE: To use various methods like grabber.videoCodec() ensure grabber.start() has been called first
     */
    fun start() {
        scope.launch {
            videoGrabber.start()
            waitForAudioToStart()
            syncWithAudio()
            var image: Frame? = videoGrabber.grabImage() // The simple grab() function may return audio frame as well
            val startTime = System.nanoTime() / 1000 - image!!.timestamp
            while (image != null) {
                while (!isResumed) delay(50.milliseconds)
                _videoTimestamp.value = videoGrabber.timestamp
                val bitmap = imageConverter.convert(image).toComposeImageBitmap()
                _videoFrames.value = bitmap
                val elapsedTime = (System.nanoTime() / 1000) - startTime
                delay((image.timestamp - elapsedTime).microseconds)
                image = videoGrabber.grabImage()
            }
            videoGrabber.stop()
        }
        scope.launch {
            audioGrabber.start()
            if (!audioGrabber.hasAudio()) return@launch
            // See https://github.com/bytedeco/javacv/blob/master/samples/JavaFxPlayVideoAndAudio.java
            val audioFormat = AudioFormat(
                // NOTE: I multiplied by 2 so the audio plays at normal speed.
                //  Why? Is it because the video is interlaced?
                // NOTE: Accessing `grabber.audioFrameRate` or `grabber.lengthInAudioFrames`
                //  speeds up the audio for some videos (including the default .ts video).
                audioGrabber.sampleRate.toFloat() * 2,
                16,
                audioGrabber.audioChannels,
                true,
                true
            )
            val audioInfo = DataLine.Info(SourceDataLine::class.java, audioFormat)
            soundLine = AudioSystem.getLine(audioInfo) as SourceDataLine
            soundLine!!.open(audioFormat, /* 8820 */ /* See https://stackoverflow.com/a/6994265 */)
            soundLine!!.start()

            var frame = audioGrabber.grabSamples()
            while (frame != null) {
                while (!isResumed) delay(50.milliseconds)

                _audioTimestamp.value = audioGrabber.timestamp
                _soundLineTimestamp.value = soundLine!!.microsecondPosition

                val channelSamplesShortBuffer = frame.samples[0] as ShortBuffer
                channelSamplesShortBuffer.rewind()
                val outBuffer = ByteBuffer.allocate(channelSamplesShortBuffer.capacity() * 2)
                for (i in 0 until channelSamplesShortBuffer.capacity()) {
                    outBuffer.putShort(channelSamplesShortBuffer[i])
                }
                soundLine!!.write(outBuffer.array(), 0, outBuffer.capacity())
                outBuffer.clear()
                frame = audioGrabber.grabSamples()
            }
        }





        // REMOVEME: Artificial pause/resume of the video
        // scope.launch {
        //     delay(5.seconds)
        //     pause()
        //     delay(3.seconds)
        //     resume()
        // }
    }

    private suspend fun waitForAudioToStart() {
        // fun isStarted() = (soundLine?.framePosition ?: 0) > 0
        // fun isStarted() = (soundLine?.microsecondPosition ?: 0) > 1_000_000
        // fun isStarted() = (soundLine?.longFramePosition ?: 0) > 0
        // fun isStarted() = soundLine?.isActive == false
        // while (!isStarted()) delay(50.milliseconds)

        var isStarted = false
        while (soundLine == null) delay(1.milliseconds)
        soundLine?.addLineListener {
            if (it.type == LineEvent.Type.START) isStarted = true
        }
        while (!isStarted) delay(5.milliseconds)
        println(soundLine?.longFramePosition)
    }

    /**
     * Alternative implementation:
     *
     * ```kotlin
     * val initialDelay = (soundLine!!.microsecondPosition)
     *     .coerceAtLeast(0)
     * delay(initialDelay)
     * ```
     */
    private fun syncWithAudio() {
        videoGrabber.timestamp = soundLine!!.microsecondPosition
    }

    fun resume() {
        soundLine!!.start()
        isResumed = true
    }

    /**
     * See https://stackoverflow.com/q/16915241
     * and https://stackoverflow.com/q/24274997
     */
    fun pause() {
        isResumed = false
        soundLine!!.stop()
    }
}
