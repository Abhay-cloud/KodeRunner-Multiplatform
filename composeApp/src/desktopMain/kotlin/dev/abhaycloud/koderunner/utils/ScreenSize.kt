package dev.abhaycloud.koderunner.utils
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalWindowInfo

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun screenSize() = ScreenDimension(height = LocalWindowInfo.current.containerSize.height, width =  LocalWindowInfo.current.containerSize.width)