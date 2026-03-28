package com.rdapps.aboutme.deviceInfo

import com.rdapps.aboutme.model.DeviceInfo
import kotlinx.browser.window

actual fun getDeviceInfo(): DeviceInfo {
    return with(window.navigator) {
        DeviceInfo(
            userAgent = userAgent,
            vendor = "$vendor $vendorSub",
            platform = platform,
            language = language
        )
    }
}