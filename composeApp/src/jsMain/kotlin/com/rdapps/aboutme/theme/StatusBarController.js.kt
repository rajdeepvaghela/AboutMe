package com.rdapps.aboutme.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import kotlinx.browser.document

@Composable
actual fun StatusBarController(isDark: Boolean) {
    val bgColor = PortfolioTheme.colors.background
    SideEffect {
        val r = (bgColor.red * 255).toInt()
        val g = (bgColor.green * 255).toInt()
        val b = (bgColor.blue * 255).toInt()
        document.body?.style?.backgroundColor = "rgb($r, $g, $b)"
    }
}
