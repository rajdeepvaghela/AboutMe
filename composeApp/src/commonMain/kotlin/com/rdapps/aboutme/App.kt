package com.rdapps.aboutme

import aboutme.composeapp.generated.resources.Montserrat_Variable
import aboutme.composeapp.generated.resources.Res
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.Font

// Shared data models
data class WorkExperience(
    val company: String,
    val location: String,
    val role: String,
    val period: String,
)

data class WorkProject(
    val title: String,
    val subtitle: String,
    val context: String,
    val achievements: List<String>,
)

data class SkillCategory(
    val category: String,
    val skills: List<String>
)

data class CompanyExperience(
    val work: WorkExperience,
    val projects: List<WorkProject>,
)

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

@Composable
fun montserratTypography() = Typography().run {

    val fontRes = Res.font.Montserrat_Variable
//    val fontRes = Res.font.OpenSans_Variable
//    val fontRes = Res.font.Nunito_Variable

    val fontFamily = FontFamily(
        Font(fontRes, FontWeight.Normal),
        Font(fontRes, FontWeight.Medium),
        Font(fontRes, FontWeight.SemiBold),
        Font(fontRes, FontWeight.Bold)
    )
    copy(
        displayLarge = displayLarge.copy(fontFamily = fontFamily),
        displayMedium = displayMedium.copy(fontFamily = fontFamily),
        displaySmall = displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = titleLarge.copy(fontFamily = fontFamily),
        titleMedium = titleMedium.copy(fontFamily = fontFamily),
        titleSmall = titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = bodyLarge.copy(fontFamily = fontFamily),
        bodyMedium = bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = bodySmall.copy(fontFamily = fontFamily),
        labelLarge = labelLarge.copy(fontFamily = fontFamily),
        labelMedium = labelMedium.copy(fontFamily = fontFamily),
        labelSmall = labelSmall.copy(fontFamily = fontFamily)
    )
}

// Design system: #121212, #303030, #A6A6A6, #F5F5F5 (reference)
private val DarkPortfolioColors = PortfolioColors(
    background = Color(0xFF000000),
    secondaryBackground = Color(0xFF303030),
    accent = Color(0xFFF5F5F5),
    accentStroke = Color(0xFFF5F5F5),
    primaryText = Color(0xFFF5F5F5),
    secondaryText = Color(0xFFA6A6A6),
    linkText = Color(0xFFA6A6A6),
)

private val LightPortfolioColors = PortfolioColors(
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
private fun ProvidePortfolioColors(
    colors: PortfolioColors,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalPortfolioColors provides colors) {
        content()
    }
}

@Composable
@Preview
fun App() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components {
                add(KtorNetworkFetcherFactory())
            }
            .build()
    }

    val isSystemInDarkTheme = isSystemInDarkTheme()
    var isDark by remember { mutableStateOf(isSystemInDarkTheme) }
    val palette = if (isDark) DarkPortfolioColors else LightPortfolioColors

    MaterialTheme(typography = montserratTypography()) {
        ProvidePortfolioColors(palette) {
            PortfolioScreen(
                isDark = isDark,
                onToggleTheme = { isDark = !isDark },
            )
        }
    }
}

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

    BoxWithConstraints(
        modifier = Modifier
            .systemBarsPadding()
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
        val isWideScreen = maxWidth > 800.dp
        val pagerState = rememberPagerState(initialPage = 0, pageCount = { Tabs.entries.size })


        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize(),
                userScrollEnabled = false
            ) { index ->
                when (index) {
                    Tabs.Intro.ordinal -> {
                        AboutSection(isWideScreen)
                    }

                    Tabs.Projects.ordinal -> {
                        ProjectSection(isWideScreen = isWideScreen)
                    }

                    Tabs.AboutMe.ordinal -> {
                        AboutMe(isWideScreen)
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
                    .padding(end = 20.dp)
            )
        }

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