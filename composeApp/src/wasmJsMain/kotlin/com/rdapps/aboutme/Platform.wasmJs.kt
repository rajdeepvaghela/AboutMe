package com.rdapps.aboutme

import kotlinx.browser.window

class WasmPlatform : Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()

actual fun openUrl(url: String) {
    window.open(url, "_blank")
}