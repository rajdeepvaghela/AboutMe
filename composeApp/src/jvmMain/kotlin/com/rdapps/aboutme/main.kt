package com.rdapps.aboutme

import aboutme.composeapp.generated.resources.Res
import aboutme.composeapp.generated.resources.ic_launcher
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.jetbrains.compose.resources.painterResource

fun main() = application {
    val state = rememberWindowState(width = 1200.dp, height = 800.dp)
    Window(
        onCloseRequest = ::exitApplication,
        title = "About Me",
        state = state,
        icon = painterResource(Res.drawable.ic_launcher)
    ) {
        App()
    }
}