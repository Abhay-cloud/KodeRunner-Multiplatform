package dev.abhaycloud.koderunner

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

internal  fun openUrl(url: String?) {
    val nsUrl = url?.let { NSURL.URLWithString(it) } ?: return
    UIApplication.sharedApplication.openURL(nsUrl)
}