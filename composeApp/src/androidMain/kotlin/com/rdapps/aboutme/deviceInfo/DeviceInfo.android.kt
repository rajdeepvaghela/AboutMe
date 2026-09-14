package com.rdapps.aboutme.deviceInfo

import android.os.Build
import com.rdapps.aboutme.BuildKonfig
import com.rdapps.aboutme.model.DeviceInfo
import java.util.Locale

actual fun getDeviceInfo(): DeviceInfo {
    return DeviceInfo(
        userAgent = "AboutMe/${BuildKonfig.VERSION_NAME} (${BuildKonfig.VERSION_CODE}; Android ${Build.VERSION.RELEASE}; ${Build.MANUFACTURER} ${Build.MODEL})",
        vendor = Build.MANUFACTURER,
        platform = "Android",
        language = Locale.getDefault().toLanguageTag()
    )
}