package com.rdapps.aboutme.examples

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.rdapps.aboutme.theme.PortfolioTheme
import com.rdapps.aboutme.utils.LocalIsWideScreen

@Composable
fun BirthdayCalendarVisual(modifier: Modifier) {
    AsyncImage(
        model = "https://play-lh.googleusercontent.com/ERrhISyBgw-0ex400_ybVDuHLVeZFLazdshPGp-DqGIeEDzvBr9BXJ_Fecl2F0SPF9wo=w1000-h2000",
        contentScale = ContentScale.Crop,
        contentDescription = null,
        modifier = modifier.clip(RoundedCornerShape(40.dp))
            .background(PortfolioTheme.colors.secondaryBackground)
            .height(if (LocalIsWideScreen.current) 300.dp else 250.dp)
    )
}