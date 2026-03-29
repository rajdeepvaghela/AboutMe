package com.rdapps.aboutme.deviceInfo

import android.os.Build
import com.rdapps.aboutme.BuildConfig
import com.rdapps.aboutme.model.DeviceInfo
import java.util.Locale

actual fun getDeviceInfo(): DeviceInfo {
    return DeviceInfo(
        userAgent = "AboutMe/${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE}; Android ${Build.VERSION.RELEASE}; ${Build.MANUFACTURER} ${Build.MODEL})",
        vendor = Build.MANUFACTURER,
        platform = "Android",
        language = Locale.getDefault().toLanguageTag()
    )
}