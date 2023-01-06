import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
            640,
            360
        )
    }
}
