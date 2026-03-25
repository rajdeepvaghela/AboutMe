package com.rdapps.aboutme.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import com.rdapps.aboutme.utils.LocalIsWideScreen

@Composable
fun PortfolioTheme(content: @Composable (isDark: Boolean, onChange: (Boolean) -> Unit) -> Unit) {
    val isSystemInDarkTheme = isSystemInDarkTheme()
    var isDark by remember { mutableStateOf(isSystemInDarkTheme) }
    val palette = if (isDark) DarkPortfolioColors else LightPortfolioColors

    MaterialTheme(typography = montserratTypography()) {
        ProvidePortfolioColors(palette) {
            StatusBarController(isDark)

            val windowWidth = LocalWindowInfo.current.containerDpSize.width
            val isWideScreen by remember(windowWidth) {
                derivedStateOf { windowWidth >= 900.dp }
            }
            CompositionLocalProvider(LocalIsWideScreen provides isWideScreen) {
                content(isDark) {
                    isDark = it
                }
            }
        }
    }
}