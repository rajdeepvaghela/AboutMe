package com.rdapps.aboutme.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Theming
data class PortfolioColors(
    val background: Color,
    val secondaryBackground: Color,
    val accent: Color,
    val accentStroke: Color,
    val primaryText: Color,
    val secondaryText: Color,
    val linkText: Color,
)

// Design system: #121212, #303030, #A6A6A6, #F5F5F5 (reference)
val DarkPortfolioColors = PortfolioColors(
    background = Color(0xFF000000),
    secondaryBackground = Color(0xFF303030),
    accent = Color(0xFFF5F5F5),
    accentStroke = Color(0xFFF5F5F5),
    primaryText = Color(0xFFF5F5F5),
    secondaryText = Color(0xFFA6A6A6),
    linkText = Color(0xFFA6A6A6),
)

val LightPortfolioColors = PortfolioColors(
    background = Color(0xFFF5F5F5),
    secondaryBackground = Color(0xFFE0E0E0),
    accent = Color(0xFF121212),
    accentStroke = Color(0xFF121212),
    primaryText = Color(0xFF121212),
    secondaryText = Color(0xFF303030),
    linkText = Color(0xFF303030),
)

private val LocalPortfolioColors = staticCompositionLocalOf { DarkPortfolioColors }

object PortfolioTheme {
    val colors: PortfolioColors
        @Composable
        @ReadOnlyComposable
        get() = LocalPortfolioColors.current
}

@Composable
fun ProvidePortfolioColors(
    colors: PortfolioColors,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalPortfolioColors provides colors) {
        content()
    }
}