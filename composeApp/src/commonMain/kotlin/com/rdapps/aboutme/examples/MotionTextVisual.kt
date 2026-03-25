package com.rdapps.aboutme.examples

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.github.panpf.sketch.AsyncImage
import com.rdapps.aboutme.utils.LocalIsWideScreen

@Composable
fun MotionTextVisual(modifier: Modifier) {
    AsyncImage(
        uri = "https://raw.githubusercontent.com/rajdeepvaghela/MotionText/refs/heads/master/motion_demo.gif",
        contentScale = ContentScale.Fit,
        contentDescription = null,
        modifier = modifier.height(if (LocalIsWideScreen.current) 300.dp else 250.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(40.dp))
            .background(Color(0xFFFCFCFF))
    )
}