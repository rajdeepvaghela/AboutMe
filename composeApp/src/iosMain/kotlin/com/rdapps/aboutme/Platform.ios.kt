package com.rdapps.aboutme

import platform.UIKit.UIApplication
import platform.UIKit.UIDevice
import platform.Foundation.NSURL

class IOSPlatform : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual fun openUrl(url: String) {
    val nsUrl = NSURL.URLWithString(url)
    nsUrl?.let {
        UIApplication.sharedApplication.openURL(
            it,
            options = emptyMap<Any?, Any>(),
            completionHandler = null
        )
    }
}