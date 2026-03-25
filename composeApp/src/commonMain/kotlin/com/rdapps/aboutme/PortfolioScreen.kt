package com.rdapps.aboutme

import androidx.compose.animation.core.Animatable
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

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { Tabs.entries.size })

    Box(
        modifier = Modifier.systemBarsPadding()
            .fillMaxSize()
            .background(PortfolioTheme.colors.background)
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                drawCircle(
                    color = backgroundStripColor.copy(alpha = 0.1f),
                    radius = size.width * 0.8f,
                    center = Offset(size.width * 1.1f, size.height * 0.15f),
                    style = Stroke(width = strokeWidth)
                )
                drawCircle(
                    color = backgroundStripColor.copy(alpha = 0.1f),
                    radius = size.width * 0.9f,
                    center = Offset(-size.width * 0.1f, size.height * 0.45f),
                    style = Stroke(width = strokeWidth)
                )
                drawCircle(
                    color = backgroundStripColor.copy(alpha = 0.1f),
                    radius = size.width * 0.7f,
                    center = Offset(size.width * 0.6f, size.height * 0.85f),
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