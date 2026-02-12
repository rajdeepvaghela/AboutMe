package com.rdapps.aboutme

import android.content.Intent
import android.net.Uri
import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

// Note: This is a no-op placeholder. To fully support opening URLs on Android,
// you can inject an application Context here and call startActivity(intent).
actual fun openUrl(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    // TODO: Use a Context to start this activity.
}