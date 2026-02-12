package com.rdapps.aboutme

import kotlinx.browser.window

class JsPlatform: Platform {
    override val name: String = "Web with Kotlin/JS"
}

actual fun getPlatform(): Platform = JsPlatform()

actual fun openUrl(url: String) {
    window.open(url, "_blank")
}