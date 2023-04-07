import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication
import java.awt.Dimension
import java.net.URL

/**
 * NOTE: The sample.ts file is inherently out of sync.
 *  Try playing it with another player (like PotPlayer) to see.
 *
 * To play a local file, use a URL notation like this:
 * ```kotlin
 * const val VIDEO_URL = "file:///C:/Users/John/Desktop/example.mp4"
 * ```
 * Relative paths like this may also work (relative to subproject directory aka `demo/`):
 * ```kotlin
 * val VIDEO_URL = """file:///${Path("videos/example.mp4")}"""
 * ```
 * To package a video with the app distributable,
 * see [this tutorial](https://github.com/JetBrains/compose-jb/tree/master/tutorials/Native_distributions_and_local_execution#adding-files-to-packaged-application)
 * and then use a URL syntax like this:
 * ```kotlin
 * val VIDEO_URL = """file:///${Path(System.getProperty("compose.application.resources.dir")) / "example.mp4"}"""
 * ```
 *
 * Other notations that worked if we use [URL] instead of string:
 *   - URL("file://192.168.12.34/path/to/video.ts")
 *   - URL("file:/Users/John/path/to/video.ts")
 *   - URL("file:///Users/John/path/to/video.ts")
 *   - URL("file:C:/Users/John/path/to/video.ts")
 *   - URL("""file:${File("raw/sample.ts")}""")
 *   - URL("""file:${Path("raw/sample.ts")}""")
 *   - URL("""file:${File("raw/sample.ts").absolutePath}""")
 *   - URL("""file:${Path("raw/sample.ts").pathString}""")
 *   - URL("file" , "" , File("raw/sample.ts").absolutePath)
 *   - etc.
 */
const val VIDEO_URL = "file:///raw/1.mp4"

fun main() {
    singleWindowApplication(title = "Video Player") {
        // See https://github.com/JetBrains/compose-multiplatform/issues/2285
        window.minimumSize = Dimension(700, 560)
        MaterialTheme {
            App()
        }
    }
}

@Preview
@Composable
fun App() {
    val state = rememberVideoPlayerState()
    /*
     * Could not use a [Box] to overlay the controls on top of the video.
     * See https://github.com/JetBrains/compose-multiplatform/tree/master/tutorials/Swing_Integration
     * Related issues:
     * https://github.com/JetBrains/compose-multiplatform/issues/1521
     * https://github.com/JetBrains/compose-multiplatform/issues/2926
     */
    Column {
        val progress by VideoPlayer(
            url = VIDEO_URL,
            state = state,
            onFinish = state::stopPlayback,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        )
        Slider(
            value = progress.fraction,
            onValueChange = { state.seek = it },
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Timestamp: ${progress.timeMillis} ms", modifier = Modifier.width(180.dp))
            IconButton(onClick = state::toggleResume) {
                Icon(
                    imageVector = if (state.isResumed) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                    contentDescription = "Play/Pause",
                    modifier = Modifier.size(32.dp)
                )
            }
            IconButton(onClick = state::toggleFullscreen) {
                Icon(
                    imageVector = if (state.isFullscreen) Icons.Rounded.FullscreenExit else Icons.Rounded.Fullscreen,
                    contentDescription = "Toggle fullscreen",
                    modifier = Modifier.size(32.dp)
                )
            }
            OutlinedTextField(
                value = state.speed.toString(),
                maxLines = 1,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Speed,
                        contentDescription = "Speed",
                        modifier = Modifier.size(28.dp)
                    )
                },
                modifier = Modifier.width(104.dp),
                onValueChange = { state.speed = it.toFloat() }
            )
            Row (verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.VolumeUp,
                    contentDescription = "Volume",
                    modifier = Modifier.size(32.dp)
                )
                // TODO: Make the slider change volume in logarithmic manner
                //  See https://www.dr-lex.be/info-stuff/volumecontrols.html
                //  and https://ux.stackexchange.com/q/79672/117386
                //  and https://dcordero.me/posts/logarithmic_volume_control.html
                Slider(
                    value = state.volume,
                    onValueChange = { state.volume = it },
                    modifier = Modifier.width(100.dp)
                )
            }
        }
    }
}
