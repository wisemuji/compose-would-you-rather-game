import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ui.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "compose-would-you-rather-game",
    ) {
        App()
    }
}
