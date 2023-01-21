import androidx.compose.runtime.*
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
import kotlin.time.Duration.Companion.milliseconds

data class Progress(val fraction: Float, val time/* millis */: Long)

@Composable
fun VideoPlayer(
    url: String,
    isResumed: Boolean,
    volume: Float = 1f,
    speed: Float = 1f,
    seek: Float = 0f,
    isFullscreen: Boolean = false,
    modifier: Modifier = Modifier,
    onFinish: (() -> Unit)? = null
): State<Progress> {
    val mediaPlayerComponent = initializeMediaPlayerComponent()
    val mediaPlayer = remember { mediaPlayerComponent.mediaPlayer() }
    // OR the following code and using SwingPanel(factory = { factory }, ...)
    // val factory  by rememberUpdatedState(mediaPlayerComponent)
    val factory = remember { { mediaPlayerComponent } }
    LaunchedEffect(url) { mediaPlayer.media().play(url) /* OR .start(url) */ }
    LaunchedEffect(seek) { mediaPlayer.controls().setPosition(seek) }
    LaunchedEffect(speed) { mediaPlayer.controls().setRate(speed) }
    LaunchedEffect(volume) { mediaPlayer.audio().setVolume((volume * 100).toInt()) }
    LaunchedEffect(isResumed) { mediaPlayer.controls().setPause(!isResumed) }
    LaunchedEffect(isFullscreen) {
        if (mediaPlayer is EmbeddedMediaPlayer) {
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
    mediaPlayer.setupVideoFinishHandler(onFinish)
    return mediaPlayer.produceProgressFor(url)
}

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
 * We play the video on finish (so the player is kind of idempotent).
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

@Composable
private fun MediaPlayer.produceProgressFor(url: String) =
    produceState(key1 = url, initialValue = Progress(0f, 0L)) {
        while (true) {
            val fraction = status().position()
            val time = status().time()
            value = Progress(fraction, time)
            delay(50.milliseconds)
        }
    }

/**
 * To return [MediaPlayer] from player components.
 * The method names are the same, but they don't share the same parent/interface.
 * That's why we need this method.
 */
private fun Any.mediaPlayer() = when (this) {
    is CallbackMediaPlayerComponent -> mediaPlayer()
    is EmbeddedMediaPlayerComponent -> mediaPlayer()
    else                            -> throw IllegalArgumentException("You can only call mediaPlayer() on vlcj player components")
}

private fun isMacOS(): Boolean {
    val os = System
        .getProperty("os.name", "generic")
        .lowercase(Locale.ENGLISH)
    return "mac" in os || "darwin" in os
}
