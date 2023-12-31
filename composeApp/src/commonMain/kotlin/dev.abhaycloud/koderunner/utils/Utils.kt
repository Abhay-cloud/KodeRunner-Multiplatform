package dev.abhaycloud.koderunner.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object Utils {
    const val platformNameAndroid = "android"
    const val platformNameIOS = "ios"
    const val platformNameDesktop = "desktop"
    fun printLogs(log: Any){
        println("CodeRunner Logs: $log")
    }

    fun String.convertDateToHumanReadableFormat(): String {
        val instant = Instant.parse(this)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
        return "${localDateTime.dayOfMonth}/${localDateTime.month.ordinal+1}/${localDateTime.year}"
    }

    @Composable
    fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }

}

data class ScreenDimension(
    val height: Int,
    val width: Int
)