package com.rdapps.aboutme.theme

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.core.view.WindowCompat

@Composable
actual fun StatusBarController(isDark: Boolean) {
    val activity = LocalActivity.current
    if (!LocalInspectionMode.current) {
        SideEffect {
            if (activity != null) {
                WindowCompat.getInsetsController(
                    activity.window,
                    activity.window.decorView
                ).isAppearanceLightStatusBars = !isDark
            }
        }
    }
}

