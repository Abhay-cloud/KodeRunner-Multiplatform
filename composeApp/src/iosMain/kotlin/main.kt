import androidx.compose.ui.window.ComposeUIViewController
import androidx.compose.ui.uikit.OnFocusBehavior
import dev.abhaycloud.koderunner.App
import dev.abhaycloud.koderunner.di.AppModule
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController(
    configure = {
        //Required for WindowInsets behaviour.
        //Analog of Android Manifest activity.windowSoftInputMode="adjustNothing"
        onFocusBehavior = OnFocusBehavior.DoNothing
    }
) { App(appModule = AppModule()) }
