package com.rdapps.aboutme.deviceInfo

import com.rdapps.aboutme.model.DeviceInfo
import java.util.Locale

actual fun getDeviceInfo(): DeviceInfo {
    val osName = System.getProperty("os.name") ?: "Unknown"
    val osVersion = System.getProperty("os.version") ?: "Unknown"
    val jvmVersion = System.getProperty("java.version") ?: "Unknown"
    return DeviceInfo(
        userAgent = "AboutMe/1.0 (Desktop; $osName $osVersion; JVM $jvmVersion)",
        vendor = osName,
        platform = "Desktop",
        language = Locale.getDefault().toLanguageTag()
    )
}

