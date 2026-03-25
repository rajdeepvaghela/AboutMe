package com.rdapps.aboutme

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.WavingHand
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rdapps.aboutme.theme.PortfolioTheme
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlinx.coroutines.launch

enum class Tabs(val title: String) {
    Intro("Hi"),
    Projects("Projects"),
    AboutMe("About Me")
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PortfolioScreen(
    isDark: Boolean,
    onToggleTheme: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val backgroundStripColor = PortfolioTheme.colors.accentStroke

    val infiniteTransition = rememberInfiniteTransition(label = "bgAnimation")
    val animAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "bgAngle"
    )

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { Tabs.entries.size })

    Box(
        modifier = Modifier.systemBarsPadding()
            .fillMaxSize()
            .background(PortfolioTheme.colors.background)
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                // Use minDimension so circles stay proportional in both portrait & landscape
                val minDim = minOf(size.width, size.height)
                val driftX = minDim * 0.04f
                val driftY = minDim * 0.03f

                // Phase offsets so each circle animates independently (120° apart)
                val phase1 = animAngle
                val phase2 = animAngle + (2 * PI / 3).toFloat()
                val phase3 = animAngle + (4 * PI / 3).toFloat()

                // Circle 1 — top-right (center at right edge so visible in both orientations)
                drawCircle(
                    color = backgroundStripColor.copy(alpha = 0.1f),
                    radius = minDim * 0.8f * (1f + sin(phase1.toDouble()).toFloat() * 0.04f),
                    center = Offset(
                        size.width * 0.85f + cos(phase1.toDouble()).toFloat() * driftX,
                        size.height * 0.1f + sin(phase1.toDouble()).toFloat() * driftY
                    ),
                    style = Stroke(width = strokeWidth)
                )
                // Circle 2 — mid-left (slightly off-screen left)
                drawCircle(
                    color = backgroundStripColor.copy(alpha = 0.1f),
                    radius = minDim * 0.9f * (1f + sin(phase2.toDouble()).toFloat() * 0.04f),
                    center = Offset(
                        -minDim * 0.1f + cos(phase2.toDouble()).toFloat() * driftX,
                        size.height * 0.45f + sin(phase2.toDouble()).toFloat() * driftY
                    ),
                    style = Stroke(width = strokeWidth)
                )
                // Circle 3 — bottom-center
                drawCircle(
                    color = backgroundStripColor.copy(alpha = 0.1f),
                    radius = minDim * 0.7f * (1f + sin(phase3.toDouble()).toFloat() * 0.04f),
                    center = Offset(
                        size.width * 0.55f + cos(phase3.toDouble()).toFloat() * driftX,
                        size.height * 0.9f + sin(phase3.toDouble()).toFloat() * driftY
                    ),
                    style = Stroke(width = strokeWidth)
                )
            }
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize(),
            userScrollEnabled = false
        ) { index ->
            when (index) {
                Tabs.Intro.ordinal -> {
                    IntroSection()
                }

                Tabs.Projects.ordinal -> {
                    ProjectSection()
                }

                Tabs.AboutMe.ordinal -> {
                    AboutMe()
                }
            }
        }

        Spacer(
            Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(100.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            PortfolioTheme.colors.background,
                            Color.Transparent
                        )
                    )
                )
        )

        Spacer(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(100.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            PortfolioTheme.colors.background
                        )
                    )
                )
        )

        Text(
            text = "Made using Compose Multiplatform",
            color = PortfolioTheme.colors.secondaryText,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.BottomEnd)
                .padding(end = 24.dp)
        )

        ThemeToggle(
            isDark = isDark,
            onToggleTheme = onToggleTheme,
            modifier = Modifier
                .padding(24.dp)
                .align(
                    Alignment.TopEnd
                )
        )

        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .padding(24.dp)
                .align(Alignment.BottomCenter)
                .defaultMinSize(minWidth = 600.dp)
        ) {
            Tabs.entries.forEachIndexed { index, tab ->
                val selected = pagerState.targetPage == index
                SegmentedButton(
                    selected = selected,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = PortfolioTheme.colors.accent,
                        activeContentColor = PortfolioTheme.colors.background,
                        activeBorderColor = PortfolioTheme.colors.accentStroke,
                        inactiveContentColor = PortfolioTheme.colors.secondaryText,
                        inactiveContainerColor = PortfolioTheme.colors.secondaryBackground
                    ),
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = Tabs.entries.size
                    ),
                    icon = {
                        SegmentedButtonDefaults.Icon(
                            active = selected,
                            activeContent = {
                                Icon(
                                    imageVector = when (tab) {
                                        Tabs.Intro -> Icons.Rounded.WavingHand
                                        Tabs.Projects -> Icons.Rounded.Code
                                        Tabs.AboutMe -> Icons.Rounded.Person
                                    },
                                    contentDescription = null
                                )
                            }
                        )
                    },
                    label = {
                        Text(
                            text = tab.title,
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
            }
        }
    }
}

// --- Layout chrome ---

@Composable
private fun ThemeToggle(
    isDark: Boolean,
    onToggleTheme: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotation = remember { Animatable(0f) }
    val scale = remember { Animatable(1f) }

    LaunchedEffect(isDark) {
        scale.animateTo(0.5f, animationSpec = tween(150))
        rotation.snapTo(0f)
        rotation.animateTo(360f, animationSpec = tween(350))
        scale.animateTo(1f, animationSpec = tween(150))
    }

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(PortfolioTheme.colors.secondaryBackground)
            .clickable { onToggleTheme() }
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isDark) Icons.Rounded.LightMode else Icons.Rounded.DarkMode,
            contentDescription = if (isDark) "Switch to Light Mode" else "Switch to Dark Mode",
            tint = PortfolioTheme.colors.primaryText,
            modifier = Modifier
                .rotate(rotation.value)
                .scale(scale.value)
        )
    }
}