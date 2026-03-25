package com.rdapps.aboutme.utils

import androidx.compose.runtime.compositionLocalOf

/**
 * CompositionLocal that provides whether the current screen is considered "wide"
 * (i.e. maxWidth > 800.dp). Provided once in [com.rdapps.aboutme.PortfolioScreen] and consumed by
 * any composable in the tree without explicit parameter passing.
 */
val LocalIsWideScreen = compositionLocalOf { false }