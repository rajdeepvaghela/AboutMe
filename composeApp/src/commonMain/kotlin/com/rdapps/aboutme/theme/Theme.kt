package com.rdapps.aboutme.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import com.rdapps.aboutme.utils.LocalIsWideScreen

@Composable
fun PortfolioTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val palette = if (isDarkTheme) DarkPortfolioColors else LightPortfolioColors

    MaterialTheme(typography = montserratTypography()) {
        ProvidePortfolioColors(palette) {
            StatusBarController(isDarkTheme)

            val windowWidth = LocalWindowInfo.current.containerDpSize.width
            val isWideScreen by remember(windowWidth) {
                derivedStateOf { windowWidth >= 900.dp }
            }
            CompositionLocalProvider(LocalIsWideScreen provides isWideScreen) {
                content()
            }
        }
    }
}