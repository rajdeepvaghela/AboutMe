package com.rdapps.aboutme.deviceInfo

import com.rdapps.aboutme.model.DeviceInfo
import platform.Foundation.NSBundle
import platform.UIKit.UIDevice

actual fun getDeviceInfo(): DeviceInfo {
    val bundle = NSBundle.mainBundle
    val appName = bundle.objectForInfoDictionaryKey("CFBundleName") as? String ?: "AboutMe"
    val versionName =
        bundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String ?: "unknown"
    val versionCode = bundle.objectForInfoDictionaryKey("CFBundleVersion") as? String ?: "0"

    val device = UIDevice.currentDevice
    val systemName = device.systemName
    val systemVersion = device.systemVersion
    val model = device.model

    return DeviceInfo(
        userAgent = "$appName/$versionName ($versionCode; $systemName $systemVersion; $model)",
        vendor = "Apple",
        platform = "iOS"
    )
}