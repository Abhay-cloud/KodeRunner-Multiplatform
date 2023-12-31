package dev.abhaycloud.koderunner

import java.awt.Desktop
import java.net.URI

internal fun openUrl(url: String?) {
    val uri = url?.let { URI.create(it) } ?: return
    Desktop.getDesktop().browse(uri)
}