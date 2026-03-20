package com.rdapps.aboutme.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.StopCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rdapps.aboutme.PortfolioTheme

@Composable
fun DemoPlayStopOverlay(
    isDemoLive: Boolean,
    onPlay: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier,
    overlayShape: Shape = RoundedCornerShape(40.dp),
    overlayTint: Color = PortfolioTheme.colors.background.copy(alpha = 0.8f),
    accentColor: Color = PortfolioTheme.colors.accent,
    playLabel: String = "Live Preview",
    stopLabel: String = "Stop",
    stopButtonPadding: Dp = 10.dp
) {
    AnimatedVisibility(visible = !isDemoLive, modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(overlayTint, overlayShape)
                .pointerInput(Unit) {}
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.align(Alignment.Center)
            ) {
                Icon(
                    imageVector = Icons.Rounded.PlayCircle,
                    contentDescription = playLabel,
                    tint = accentColor,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .clickable { onPlay() }
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = playLabel,
                    fontWeight = FontWeight.Medium,
                    color = PortfolioTheme.colors.primaryText
                )
            }
        }
    }

    AnimatedVisibility(visible = isDemoLive, modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(stopButtonPadding)
                    .align(Alignment.TopEnd)
                    .clip(RoundedCornerShape(50))
                    .clickable { onStop() }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.StopCircle,
                    contentDescription = stopLabel,
                    tint = accentColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stopLabel,
                    fontWeight = FontWeight.Medium,
                    color = PortfolioTheme.colors.primaryText
                )
            }
        }
    }
}