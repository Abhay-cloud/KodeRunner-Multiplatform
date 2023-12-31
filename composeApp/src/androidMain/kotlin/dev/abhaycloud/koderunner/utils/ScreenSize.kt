package dev.abhaycloud.koderunner.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import dev.abhaycloud.koderunner.utils.ScreenDimension

@Composable
actual fun screenSize(): ScreenDimension = ScreenDimension(height = LocalConfiguration.current.screenHeightDp, width = LocalConfiguration.current.screenWidthDp)