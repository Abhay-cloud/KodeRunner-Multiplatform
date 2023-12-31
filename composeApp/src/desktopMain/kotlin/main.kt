import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.abhaycloud.koderunner.App
import dev.abhaycloud.koderunner.di.AppModule
import java.awt.Dimension

fun main() = application {
    Window(
        title = "KodeRunner",
//        icon = painterResource("app_logo.png"),
        state = rememberWindowState(width = 1280.dp, height = 720.dp),
         resizable = false,
        onCloseRequest = ::exitApplication,
    ) {
        window.minimumSize = Dimension(1024, 600)
        window.maximumSize = Dimension(1280, 720)
        App(appModule = AppModule())
    }
}