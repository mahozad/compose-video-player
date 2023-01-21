import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

/**
 * NOTE: The sample.ts file is inherently out of sync.
 *  Try playing it with another player (like PotPlayer) to see.
 *
 * Other notations that worked for URL:
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
private const val URL = "file:///raw/1.mp4"

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}

@Preview
@Composable
fun App() {
    var seek by remember { mutableStateOf(0f) }
    var speed by remember { mutableStateOf(1f) }
    var volume by remember { mutableStateOf(1f) }
    var isResumed by remember { mutableStateOf(false) }
    var isFullscreen by remember { mutableStateOf(false) }
    val stopPlayback = remember { { isResumed = false } }
    val toggleResume = remember { { isResumed = !isResumed } }
    val toggleFullscreen = remember { { isFullscreen = !isFullscreen } }
    Column {
        val progress by VideoPlayer(
            url = URL,
            seek = seek,
            speed = speed,
            volume = volume,
            isResumed = isResumed,
            isFullscreen = isFullscreen,
            onFinish = stopPlayback,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        )
        Slider(
            value = progress.fraction,
            onValueChange = { seek = it },
            modifier = Modifier.fillMaxWidth()
        )
        Text(progress.time.toString())
        Spacer(Modifier.height(20.dp))
        Row {
            Button(onClick = toggleResume) {
                Text(if (isResumed) "Pause" else "Play")
            }
            Button(onClick = toggleFullscreen) {
                Text("${if (isFullscreen) "Exit" else "Enter"} fullscreen")
            }
            TextField(
                value = speed.toString(),
                maxLines = 1,
                leadingIcon = { Text("Speed: ") },
                onValueChange = { speed = it.toFloat() }
            )
            Slider(
                value = volume,
                onValueChange = { volume = it },
                modifier = Modifier.width(100.dp)
            )
        }
    }
}
