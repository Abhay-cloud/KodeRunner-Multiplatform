package dev.abhaycloud.koderunner

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import dev.abhaycloud.koderunner.di.AppModule
import dev.abhaycloud.koderunner.screens.SplashScreen

@Composable
fun App(appModule: AppModule)  {
    MaterialTheme {
        Navigator(SplashScreen(appModule)){
            SlideTransition(it)
        }
    }
}
