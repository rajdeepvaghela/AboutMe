package com.rdapps.aboutme.examples

import aboutme.composeapp.generated.resources.Res
import aboutme.composeapp.generated.resources.ic_battery_tools
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rdapps.aboutme.utils.LocalIsWideScreen
import org.jetbrains.compose.resources.painterResource

@Composable
fun BatteryToolsVisual(modifier: Modifier) {
    Box(
        modifier.fillMaxWidth().height(if (LocalIsWideScreen.current) 300.dp else 250.dp)
            .clip(RoundedCornerShape(40.dp))
            .background(Color(0xFF404F7F)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_battery_tools),
            contentDescription = null
        )
    }
}