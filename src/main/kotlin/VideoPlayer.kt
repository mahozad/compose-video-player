import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery
import uk.co.caprica.vlcj.player.base.MediaPlayer
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter
import uk.co.caprica.vlcj.player.component.CallbackMediaPlayerComponent
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer
import java.awt.Component
import java.util.*
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

data class Progress(val fraction: Float, val time: Duration)

@Composable
private fun VideoPlayerImpl(
    url: String,
    isResumed: Boolean,
    volume: Float,
    speed: Float,
    seek: Float,
    isFullscreen: Boolean,
    modifier: Modifier,
    onFinish: (() -> Unit)?
): State<Progress> {
    val mediaPlayerComponent = initializeMediaPlayerComponent()
    val mediaPlayer = remember { mediaPlayerComponent.mediaPlayer() }
    mediaPlayer.setupVideoFinishHandler(onFinish)

    val factory = remember { { mediaPlayerComponent } }
    /* OR the following code and using SwingPanel(factory = { factory }, ...) */
    // val factory by rememberUpdatedState(mediaPlayerComponent)

    LaunchedEffect(url) { mediaPlayer.media().play(url) /* OR .start(url) */ }
    LaunchedEffect(seek) { mediaPlayer.controls().setPosition(seek) }
    LaunchedEffect(speed) { mediaPlayer.controls().setRate(speed) }
    LaunchedEffect(volume) { mediaPlayer.audio().setVolume(volume.toPercentage()) }
    LaunchedEffect(isResumed) { mediaPlayer.controls().setPause(!isResumed) }
    LaunchedEffect(isFullscreen) {
        if (mediaPlayer is EmbeddedMediaPlayer) {
            /*
             * To be able to access window in the commented code below,
             * extend the player composable function from WindowScope.
             * See https://github.com/JetBrains/compose-multiplatform/issues/176#issuecomment-812514936
             * and its subsequent comments.
             *
             * We could also just fullscreen the whole window:
             * `window.placement = WindowPlacement.Fullscreen`
             * See https://github.com/JetBrains/compose-multiplatform/issues/1489
             */
            // mediaPlayer.fullScreen().strategy(ExclusiveModeFullScreenStrategy(window))
            mediaPlayer.fullScreen().toggle()
        }
    }
    DisposableEffect(Unit) { onDispose { mediaPlayer.release() } }
    SwingPanel(
        factory = factory,
        background = Color.Transparent,
        modifier = modifier
    )
    return mediaPlayer.produceProgress()
}

private fun Float.toPercentage() = (this * 100).roundToInt()

/**
 * See https://github.com/caprica/vlcj/issues/887#issuecomment-503288294
 * for why we're using CallbackMediaPlayerComponent for macOS.
 */
@Composable
private fun initializeMediaPlayerComponent(): Component = remember {
    NativeDiscovery().discover()
    if (isMacOS()) {
        CallbackMediaPlayerComponent()
    } else {
        EmbeddedMediaPlayerComponent()
    }
}

/**
 * We play the video on finish (so the player is kind of idempotent),
 * unless the [onFinish] callback stops the playback.
 * Using `mediaPlayer.controls().repeat = true` did not work as expected.
 */
@Composable
private fun MediaPlayer.setupVideoFinishHandler(onFinish: (() -> Unit)?) =
    DisposableEffect(onFinish) {
        val listener = object : MediaPlayerEventAdapter() {
            override fun stopped(mediaPlayer: MediaPlayer) {
                onFinish?.invoke()
                mediaPlayer.controls().play()
            }
        }
        events().addMediaPlayerEventListener(listener)
        onDispose { events().removeMediaPlayerEventListener(listener) }
    }

/**
 * Checks for and emits video progress every 50 milliseconds.
 * Note that it seems vlcj updates the progress only every 250 milliseconds or so.
 *
 * Instead of using `Unit` as the `key1` for [produceState],
 * we could use `media().info()?.mrl()` if it's needed to re-launch
 * the producer (for whatever reason) when the url (aka video) changes.
 *
 */
@Composable
private fun MediaPlayer.produceProgress() =
    produceState(key1 = Unit, initialValue = Progress(0f, 0.milliseconds)) {
        while (true) {
            val fraction = status().position()
            val time = status().time().milliseconds
            value = Progress(fraction, time)
            delay(50.milliseconds)
        }
    }

/**
 * Returns [MediaPlayer] from player components.
 * The method names are the same, but they don't share the same parent/interface.
 * That's why we need this method.
 */
private fun Component.mediaPlayer() = when (this) {
    is CallbackMediaPlayerComponent -> mediaPlayer()
    is EmbeddedMediaPlayerComponent -> mediaPlayer()
    else -> error("mediaPlayer() can only be called on vlcj player components")
}

private fun isMacOS(): Boolean {
    val os = System
        .getProperty("os.name", "generic")
        .lowercase(Locale.ENGLISH)
    return "mac" in os || "darwin" in os
}

@Composable
fun VideoPlayer(
    url: String,
    state: VideoPlayerState,
    modifier: Modifier = Modifier,
    onFinish: (() -> Unit)? = null
) = VideoPlayerImpl(
    url = url,
    isResumed = state.isResumed,
    volume = state.volume,
    speed = state.speed,
    seek = state.seek,
    isFullscreen = state.isFullscreen,
    modifier = modifier,
    onFinish = onFinish
)

@Composable
fun rememberVideoPlayerState(
    seek: Float = 0f,
    speed: Float = 1f,
    volume: Float = 1f,
    isResumed: Boolean = true,
    isFullscreen: Boolean = false
): VideoPlayerState = rememberSaveable(saver = VideoPlayerState.Saver()) {
    VideoPlayerState(
        seek,
        speed,
        volume,
        isResumed,
        isFullscreen
    )
}

class VideoPlayerState(
    seek: Float = 0f,
    speed: Float = 1f,
    volume: Float = 1f,
    isResumed: Boolean = true,
    isFullscreen: Boolean = false
) {

    var seek by mutableStateOf(seek)
    var speed by mutableStateOf(speed)
    var volume by mutableStateOf(volume)
    var isResumed by mutableStateOf(isResumed)
    var isFullscreen by mutableStateOf(isFullscreen)

    fun toggleResume() {
        isResumed = !isResumed
    }

    fun toggleFullscreen() {
        isFullscreen = !isFullscreen
    }

    fun stopPlayback() {
        isResumed = false
    }

    companion object {
        /**
         * The default [Saver] implementation for [VideoPlayerState].
         */
        fun Saver() = listSaver<VideoPlayerState, Any>(
            save = {
                listOf(
                    it.seek,
                    it.speed,
                    it.volume,
                    it.isResumed,
                    it.isFullscreen
                )
            },
            restore = {
                VideoPlayerState(
                    seek = it[0] as Float,
                    speed = it[1] as Float,
                    volume = it[2] as Float,
                    isResumed = it[3] as Boolean,
                    isFullscreen = it[3] as Boolean
                )
            }
        )
    }
}
