import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.net.URL

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}

@Preview
@Composable
fun App() {
    Column {
        val progress = remember { mutableStateOf(0f) }
        val seek = remember { mutableStateOf(0f) }
        Text("Hello, World!")
        VideoPlayer(
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
            URL("file:raw/1.mp4"),
            seek = seek.value,
            progress = progress,
            width = 640,
            height = 360
        )
        Progseek(seek, progress)
    }
}

@Composable
fun Progseek(seek: MutableState<Float>, progress: State<Float>) {
    var isSeeking by remember { mutableStateOf(false) }
    var seekValue by remember { mutableStateOf(0f) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Slider(
            value = if (isSeeking) seekValue else progress.value,
            onValueChange = {
                isSeeking = true
                seekValue = it
            },
            onValueChangeFinished = {
                seek.value = seekValue
                isSeeking = false
            }
        )
    }
}
