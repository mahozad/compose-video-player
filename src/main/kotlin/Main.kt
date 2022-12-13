import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

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
            // NOTE: The sample.ts file is inherently out of sync.
            //  Try playing it with another player to see this
            "raw/sample.ts",
            600,
            400
        )
    }
}
