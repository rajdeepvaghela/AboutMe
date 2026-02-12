package com.rdapps.aboutme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

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
    val sidebar: Color,
    val sectionDivider: Color,
    val accent: Color,
    val accentStroke: Color,
    val primaryText: Color,
    val secondaryText: Color,
    val linkText: Color,
)

private val DarkPortfolioColors = PortfolioColors(
    background = Color(0xFF050814),
    sidebar = Color(0xFF070D1A),
    sectionDivider = Color(0xFF1E293B),
    accent = Color(0xFFFFC857),
    accentStroke = Color(0xFFFFD56B),
    primaryText = Color(0xFFF9FAFB),
    secondaryText = Color(0xFF9CA3AF),
    linkText = Color(0xFF38BDF8),
)

private val LightPortfolioColors = PortfolioColors(
    background = Color(0xFFF9FAFB),
    sidebar = Color(0xFFE5E7EB),
    sectionDivider = Color(0xFFD1D5DB),
    accent = Color(0xFF2563EB),
    accentStroke = Color(0xFF1D4ED8),
    primaryText = Color(0xFF111827),
    secondaryText = Color(0xFF4B5563),
    linkText = Color(0xFF2563EB),
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
    var isDark by remember { mutableStateOf(true) }
    val palette = if (isDark) DarkPortfolioColors else LightPortfolioColors

    MaterialTheme {
        ProvidePortfolioColors(palette) {
            PortfolioScreen(
                isDark = isDark,
                onToggleTheme = { isDark = !isDark },
            )
        }
    }
}

@Composable
fun PortfolioScreen(
    isDark: Boolean,
    onToggleTheme: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val aboutRequester = remember { BringIntoViewRequester() }
    val experienceRequester = remember { BringIntoViewRequester() }
    val openSourceRequester = remember { BringIntoViewRequester() }
    val personalProjectsRequester = remember { BringIntoViewRequester() }
    val skillsRequester = remember { BringIntoViewRequester() }
    val educationRequester = remember { BringIntoViewRequester() }
    val contactRequester = remember { BringIntoViewRequester() }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(PortfolioTheme.colors.background)
    ) {
        val isWideScreen = maxWidth > 900.dp
        val scrollState = rememberScrollState()

        if (isWideScreen) {
            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                Sidebar(
                    modifier = Modifier
                        .width(260.dp)
                        .fillMaxHeight(),
                    onAboutClick = { scope.launch { aboutRequester.bringIntoView() } },
                    onSkillsClick = { scope.launch { skillsRequester.bringIntoView() } },
                    onProjectsClick = { scope.launch { personalProjectsRequester.bringIntoView() } },
                    onExperienceClick = { scope.launch { experienceRequester.bringIntoView() } },
                    onEducationClick = { scope.launch { educationRequester.bringIntoView() } },
                    onContactClick = { scope.launch { contactRequester.bringIntoView() } },
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 40.dp, vertical = 32.dp)
                ) {
                    ThemeToggleRow(
                        isDark = isDark,
                        onToggleTheme = onToggleTheme
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    HeroSection()
                    SectionDivider()
                    Box(Modifier.bringIntoViewRequester(aboutRequester).fillMaxWidth())
                    AboutSection()
                    SectionDivider()
                    Box(Modifier.bringIntoViewRequester(experienceRequester).fillMaxWidth())
                    ExperienceAndSkillsSection(
                        skillsRequester = skillsRequester,
                        isWideScreen = isWideScreen
                    )
                    SectionDivider()
                    Box(Modifier.bringIntoViewRequester(openSourceRequester).fillMaxWidth())
                    OpenSourceSection()
                    SectionDivider()
                    Box(Modifier.bringIntoViewRequester(personalProjectsRequester).fillMaxWidth())
                    PersonalProjectsSection()
                    SectionDivider()
                    Box(Modifier.bringIntoViewRequester(educationRequester).fillMaxWidth())
                    EducationSection()
                    SectionDivider()
                    Box(Modifier.bringIntoViewRequester(contactRequester).fillMaxWidth())
                    ContactSection()
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(PortfolioTheme.colors.background)
            ) {
                TopBar(
                    isDark = isDark,
                    onToggleTheme = onToggleTheme,
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(scrollState)
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                ) {
                    HeroSection()
                    SectionDivider()
                    Box(Modifier.bringIntoViewRequester(aboutRequester).fillMaxWidth())
                    AboutSection()
                    SectionDivider()
                    Box(Modifier.bringIntoViewRequester(experienceRequester).fillMaxWidth())
                    ExperienceAndSkillsSection(
                        skillsRequester = skillsRequester,
                        isWideScreen = isWideScreen
                    )
                    SectionDivider()
                    Box(Modifier.bringIntoViewRequester(openSourceRequester).fillMaxWidth())
                    OpenSourceSection()
                    SectionDivider()
                    Box(Modifier.bringIntoViewRequester(personalProjectsRequester).fillMaxWidth())
                    PersonalProjectsSection()
                    SectionDivider()
                    Box(Modifier.bringIntoViewRequester(educationRequester).fillMaxWidth())
                    EducationSection()
                    SectionDivider()
                    Box(Modifier.bringIntoViewRequester(contactRequester).fillMaxWidth())
                    ContactSection()
                }
            }
        }
    }
}

// --- Layout chrome ---

@Composable
private fun Sidebar(
    modifier: Modifier = Modifier,
    onAboutClick: () -> Unit,
    onSkillsClick: () -> Unit,
    onProjectsClick: () -> Unit,
    onExperienceClick: () -> Unit,
    onEducationClick: () -> Unit,
    onContactClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .background(PortfolioTheme.colors.sidebar)
            .padding(horizontal = 20.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            // Profile placeholder
            Box(
                modifier = Modifier
                    .width(64.dp)
                    .height(64.dp)
                    .background(
                        PortfolioTheme.colors.sectionDivider,
                        shape = MaterialTheme.shapes.small
                    ),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Rajdeep\nVaghela",
                color = PortfolioTheme.colors.primaryText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(24.dp))

            SidebarNavItem("About Me", onAboutClick)
            SidebarNavItem("Skills", onSkillsClick)
            SidebarNavItem("Projects", onProjectsClick)
            SidebarNavItem("Experience", onExperienceClick)
            SidebarNavItem("Education", onEducationClick)
            SidebarNavItem("Contact Me", onContactClick)
        }

        Text(
            text = getPlatform().name,
            color = PortfolioTheme.colors.secondaryText,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun SidebarNavItem(
    label: String,
    onClick: () -> Unit,
) {
    Text(
        text = label,
        color = PortfolioTheme.colors.secondaryText,
        fontSize = 13.sp,
        modifier = Modifier
            .padding(vertical = 6.dp)
            .clickable { onClick() }
    )
}

@Composable
private fun TopBar(
    isDark: Boolean,
    onToggleTheme: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PortfolioTheme.colors.sidebar)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Rajdeep Vaghela",
            color = PortfolioTheme.colors.primaryText,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        ThemeToggle(
            isDark = isDark,
            onToggleTheme = onToggleTheme
        )
    }
}

@Composable
private fun ThemeToggleRow(
    isDark: Boolean,
    onToggleTheme: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ThemeToggle(isDark = isDark, onToggleTheme = onToggleTheme)
    }
}

@Composable
private fun ThemeToggle(
    isDark: Boolean,
    onToggleTheme: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = if (isDark) "Dark" else "Light",
            color = PortfolioTheme.colors.secondaryText,
            fontSize = 12.sp
        )
        Switch(
            checked = isDark,
            onCheckedChange = { onToggleTheme() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = PortfolioTheme.colors.accent,
                checkedTrackColor = PortfolioTheme.colors.accent.copy(alpha = 0.6f),
                uncheckedThumbColor = PortfolioTheme.colors.sectionDivider,
                uncheckedTrackColor = PortfolioTheme.colors.sectionDivider.copy(alpha = 0.7f)
            )
        )
    }
}

@Composable
private fun SectionDivider() {
    Spacer(modifier = Modifier.height(32.dp))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(PortfolioTheme.colors.sectionDivider)
    )
    Spacer(modifier = Modifier.height(32.dp))
}

// --- Sections ---

@Composable
private fun HeroSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "HI",
            color = PortfolioTheme.colors.primaryText,
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 4.sp
        )
        Text(
            text = "I'M RAJDEEP",
            color = PortfolioTheme.colors.primaryText,
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 4.sp
        )
        Text(
            text = "ANDROID DEVELOPER",
            color = PortfolioTheme.colors.primaryText,
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 4.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Senior Android Engineer, UI-focused developer",
            color = PortfolioTheme.colors.secondaryText,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(24.dp))

        val uriHandler = LocalUriHandler.current

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            PrimaryButton(label = "Contact me") {
                uriHandler.openUri("mailto:rajdeep.vaghela610@gmail.com")
            }

            OutlinedAccentButton(label = "View LinkedIn") {
                uriHandler.openUri("https://linkedin.com/in/rajdeepvaghela")
            }
        }
    }
}

@Composable
private fun AboutSection() {
    SectionHeader("ABOUT ME")
    Spacer(modifier = Modifier.height(12.dp))
    Text(
        text = "I'm a Senior Android Engineer with over 9 years of experience building modern, performant applications across Android, iOS (via Kotlin Multiplatform) and the Web.",
        color = PortfolioTheme.colors.secondaryText,
        fontSize = 14.sp,
        lineHeight = 22.sp
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = "I enjoy leading technical modernization, crafting delightful UI using Jetpack Compose, and shipping production-ready apps with strong attention to performance and detail.",
        color = PortfolioTheme.colors.secondaryText,
        fontSize = 14.sp,
        lineHeight = 22.sp
    )
}

@Composable
private fun CompanyExperienceItem(
    companyExperience: CompanyExperience,
    isExpanded: Boolean,
    onToggle: () -> Unit,
) {
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 90f else 0f
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .background(
                color = PortfolioTheme.colors.sidebar.copy(
                    alpha = if (isExpanded) 0.7f else 0.4f
                ),
                shape = MaterialTheme.shapes.small
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = companyExperience.work.company,
                    color = PortfolioTheme.colors.primaryText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = companyExperience.work.role,
                    color = PortfolioTheme.colors.secondaryText,
                    fontSize = 14.sp
                )
                Text(
                    text = companyExperience.work.location,
                    color = PortfolioTheme.colors.secondaryText,
                    fontSize = 13.sp
                )
                Text(
                    text = companyExperience.work.period,
                    color = PortfolioTheme.colors.secondaryText,
                    fontSize = 12.sp
                )
            }

            Text(
                text = "▶",
                color = PortfolioTheme.colors.secondaryText,
                fontSize = 16.sp,
                modifier = Modifier.rotate(rotation)
            )
        }

        AnimatedVisibility(
            visible = isExpanded && companyExperience.projects.isNotEmpty(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column {
                Spacer(modifier = Modifier.height(12.dp))
                companyExperience.projects.forEach { project ->
                    Text(
                        text = project.title,
                        color = PortfolioTheme.colors.primaryText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (project.context.isNotEmpty()) {
                        Text(
                            text = project.context,
                            color = PortfolioTheme.colors.secondaryText,
                            fontSize = 12.sp
                        )
                    }
                    Text(
                        text = project.subtitle,
                        color = PortfolioTheme.colors.secondaryText,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    project.achievements.forEach { achievement ->
                        Row {
                            Text(
                                text = "• ",
                                color = PortfolioTheme.colors.primaryText,
                                fontSize = 12.sp
                            )
                            Text(
                                text = achievement,
                                color = PortfolioTheme.colors.secondaryText,
                                fontSize = 12.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
private fun ExperienceAndSkillsSection(
    skillsRequester: BringIntoViewRequester,
    isWideScreen: Boolean,
) {
    if (isWideScreen) {
        // Wide screen: Experience and Skills side by side
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // Experience takes 60% of width
            Column(
                modifier = Modifier.weight(0.6f)
            ) {
                SectionHeader("EXPERIENCE")
                Spacer(modifier = Modifier.height(16.dp))
                ExperienceContent()
            }

            // Skills takes 40% of width
            Column(
                modifier = Modifier.weight(0.4f)
            ) {
                Box(Modifier.bringIntoViewRequester(skillsRequester).fillMaxWidth())
                SectionHeader("MY SKILLS")
                Spacer(modifier = Modifier.height(16.dp))
                SkillsContent()
            }
        }
    } else {
        // Mobile: Stacked vertically (Experience first, then Skills)
        Column {
            SectionHeader("EXPERIENCE")
            Spacer(modifier = Modifier.height(16.dp))
            ExperienceContent()

            Spacer(modifier = Modifier.height(32.dp))
            Box(Modifier.bringIntoViewRequester(skillsRequester).fillMaxWidth())
            SectionHeader("MY SKILLS")
            Spacer(modifier = Modifier.height(16.dp))
            SkillsContent()
        }
    }
}

@Composable
private fun ExperienceContent() {
    val companyExperiences = listOf(
        CompanyExperience(
            work = WorkExperience(
                "Red Elk Studios",
                "Hyderabad, India (Remote)",
                "Senior Android Engineer",
                "Aug 2024 - Present"
            ),
            projects = listOf(
                WorkProject(
                    "IMVU",
                    "Social Chat & Avatar application",
                    "Client at Red Elk Studios",
                    listOf(
                        "Led modernization using Kotlin Flows and Jetpack Compose UI components.",
                        "Refactored legacy Socket connection architecture to OkHttp.",
                        "Migrated core features into MVI / MVVM architecture with Compose.",
                        "Delivered new features and maintained existing experiences."
                    )
                )
            )
        ),
        CompanyExperience(
            work = WorkExperience(
                "Holofy",
                "South Bank, England (Remote)",
                "Senior Android Engineer",
                "Apr 2020 - Apr 2024"
            ),
            projects = listOf(
                WorkProject(
                    "Holofy Spaces",
                    "Interactive video SaaS for web",
                    "Mobile and Android TV",
                    listOf(
                        "Implemented video compression pipelines using FFmpeg and MediaCodec.",
                        "Designed and built a reusable Compose UI Kit.",
                        "Integrated 100ms live streaming SDK with low-latency playback.",
                        "Shipped an Android TV module optimized for large screens."
                    )
                ),
                WorkProject(
                    "Pro Caller, Pro Emailer & Pro Scheduler",
                    "AI-powered utility applications",
                    "",
                    listOf(
                        "Architected Single-Activity apps fully in Jetpack Compose.",
                        "Integrated Twilio SDK for communication features.",
                        "Designed a cohesive design system with theme support."
                    )
                )
            )
        ),
        CompanyExperience(
            work = WorkExperience(
                "NoBroker.com",
                "Bangalore, India",
                "Senior Android Engineer",
                "May 2018 - Mar 2020"
            ),
            projects = listOf(
                WorkProject(
                    "NoBroker Property Rent & Sale",
                    "Real estate and home services-based application.",
                    "NoBroker.com",
                    listOf(
                        "Owned the Android app and led the team for a brief period.",
                        "Developed a Hybrid WebView with custom caching for Dynamic Delivery, forming the foundation for many future features.",
                        "Introduced and migrated core features from Java to Kotlin.",
                        "Improved crash-free user rate to 99.4%.",
                        "Integrated JusPay, RazorPay, and Paytm SDKs for payments, Truecaller SDK for sign up / sign in, and Adobe, Firebase, and Facebook SDKs for analytics."
                    )
                )
            )
        ),
        CompanyExperience(
            work = WorkExperience(
                "Vyapar Tech Solutions",
                "Bangalore, India",
                "Android Engineer",
                "Dec 2016 - Apr 2018"
            ),
            projects = listOf(
                WorkProject(
                    "Vyapar - Billing App GST Invoice Maker",
                    "Billing and GST invoicing application.",
                    "Vyapar Tech Solutions",
                    listOf(
                        "Integrated hardware components like Thermal Printers and USB Barcode Scanners.",
                        "Developed plugin applications to reduce the size of the main app.",
                        "Revamped the overall app design using Material Components.",
                        "Integrated SDKs such as CleverTap, iTextPdf, and Branch.io."
                    )
                )
            )
        ),
        CompanyExperience(
            work = WorkExperience(
                "Axaram Codelabs",
                "Rajkot, Gujarat",
                "Android and UI/UX Designer",
                "Oct 2015 - May 2016"
            ),
            projects = emptyList()
        )
    )

    var expandedCompany by remember { mutableStateOf<String?>(null) }

    companyExperiences.forEach { companyExperience ->
        val isExpanded = expandedCompany == companyExperience.work.company
        CompanyExperienceItem(
            companyExperience = companyExperience,
            isExpanded = isExpanded,
            onToggle = {
                expandedCompany = if (isExpanded) null else companyExperience.work.company
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun SkillsContent() {
    val skillCategories = listOf(
        SkillCategory(
            "Technologies",
            listOf(
                "AndroidX", "Kotlin Coroutines", "Kotlin Flows", "Dagger2", "Hilt",
                "RxJava", "Jetpack Compose UI-Kit", "Kotlin KTS",
                "Compose Navigation / Destinations", "Material2/3",
                "CameraX", "ExoPlayer", "Media Store", "Retrofit2",
                "OkHttp SSE", "Volley", "PiP", "Data & View Binding",
                "FFmpeg", "MediaCodec", "Work Manager",
                "WebView JavaScriptInterface", "Crashlytics", "CI/CD", "Git", "FCM", "REST APIs"
            )
        ),
        SkillCategory(
            "Payment Integrations",
            listOf("PlayStore Billing", "Paytm", "Razorpay", "Juspay", "UPI Intent")
        ),
        SkillCategory(
            "Databases",
            listOf("Room", "Firebase Realtime DB", "SQLite", "DataStore", "MySql")
        ),
        SkillCategory(
            "Third‑Party Libraries",
            listOf(
                "100ms", "Twilio", "Truecaller", "Moengage", "Adobe Analytics",
                "Applovin", "Segment", "Adjust", "Intercom", "Mixpanel"
            )
        ),
        SkillCategory(
            "Others",
            listOf(
                "Figma", "Gravit", "Adobe Photoshop", "Illustrator",
                "Autodesk 3ds Max", "Pixologic ZBrush"
            )
        )
    )

    skillCategories.forEach { category ->
        Text(
            text = category.category,
            color = PortfolioTheme.colors.primaryText,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = category.skills.joinToString(separator = "  •  "),
            color = PortfolioTheme.colors.secondaryText,
            fontSize = 13.sp,
            lineHeight = 20.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun OpenSourceSection() {
    SectionHeader("OPEN-SOURCE LIBRARIES")
    Spacer(modifier = Modifier.height(16.dp))

    val libraries = listOf(
        "Value Picker Slider" to "Customisable horizontal slider value picker built fully in Jetpack Compose.",
        "ViewSlider" to "Horizontal view slider which snaps the middle item with a scale effect.",
        "VerticalStepper" to "Customizable vertical stepper with animations and custom content layouts.",
        "CircularList" to "Vertical scrollable value picker for Jetpack Compose with InfiniteCircularList and CircularList components.",
        "MotionText" to "TextView optimised for MotionLayout transitions with additional features.",
        "TypeWriter" to "TextView and EditText with a customizable typewriter animation.",
        "Flow Layouts" to "Flow-type layout container with RadioButton support.",
        "Generic Adapter" to "Wrapper for Spinner and AutoCompleteTextView adapters to reduce boilerplate.",
        "Uncheckable RadioButton" to "RadioButton implementation that can be unchecked on click, fixing a long-standing Android limitation."
    )

    libraries.forEach { (name, description) ->
        Text(
            text = name,
            color = PortfolioTheme.colors.primaryText,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = description,
            color = PortfolioTheme.colors.secondaryText,
            fontSize = 13.sp,
            lineHeight = 20.sp
        )
        Spacer(modifier = Modifier.height(14.dp))
    }
}

@Composable
private fun PersonalProjectsSection() {
    SectionHeader("PERSONAL PROJECTS")
    Spacer(modifier = Modifier.height(16.dp))

    val projects = listOf(
        "કંકોત્રી - Wedding Invitation" to "A personalized wedding invitation website (CMP - Android & Web) with analytics, remote control over features, and an Android app that generates unique invitation links.",
        "Birthday Calendar" to "Mobile and WearOS app that syncs Facebook birthdays and enables one-tap wishes via WhatsApp, Facebook Messenger, SMS, or call, with a companion WearOS app.",
        "SocialHub" to "Android app that wraps Facebook, Twitter, Messenger, TweetDeck, and more into a single experience.",
        "Cross and Zero: Tic Tac Toe" to "Classic tic-tac-toe game refreshed with a new modern UI."
    )

    projects.forEach { (name, description) ->
        Text(
            text = name,
            color = PortfolioTheme.colors.primaryText,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = description,
            color = PortfolioTheme.colors.secondaryText,
            fontSize = 13.sp,
            lineHeight = 20.sp
        )
        Spacer(modifier = Modifier.height(14.dp))
    }
}

@Composable
private fun EducationSection() {
    SectionHeader("EDUCATION")
    Spacer(modifier = Modifier.height(12.dp))
    Text(
        text = "B.E (Computer Science), 8.49 CGPA",
        color = PortfolioTheme.colors.primaryText,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold
    )
    Text(
        text = "Atmiya Institute of Technology and Science, Rajkot, Gujarat (2012 – 2016)",
        color = PortfolioTheme.colors.secondaryText,
        fontSize = 13.sp
    )
}

@Composable
private fun ContactSection() {
    SectionHeader("CONTACT ME")
    Spacer(modifier = Modifier.height(12.dp))

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        ContactItem(text = "+91 9033159066", url = null)
        ContactItem(
            text = "rajdeep.vaghela610@gmail.com",
            url = "mailto:rajdeep.vaghela610@gmail.com"
        )
        ContactItem(
            text = "linkedin.com/in/rajdeepvaghela",
            url = "https://linkedin.com/in/rajdeepvaghela"
        )
        ContactItem(
            text = "github.com/rajdeepvaghela",
            url = "https://github.com/rajdeepvaghela"
        )
        ContactItem(
            text = "PlayStore profile",
            url = "https://play.google.com/store/apps/developer?id=Rajdeep+Vaghela"
        )
    }
}

// --- Reusable bits ---

@Composable
private fun SectionHeader(text: String) {
    Column {
        Text(
            text = text,
            color = PortfolioTheme.colors.primaryText,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 6.sp
        )
    }
}

@Composable
private fun PrimaryButton(label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = PortfolioTheme.colors.accent,
            contentColor = Color.Black
        )
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun OutlinedAccentButton(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 1.dp, vertical = 1.dp)
    ) {
        Box(
            modifier = Modifier
                .background(Color.Transparent)
                .padding(horizontal = 18.dp, vertical = 10.dp)
        ) {
            Text(
                text = label,
                color = PortfolioTheme.colors.accentStroke,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun ContactItem(text: String, url: String?) {
    var triggerUrl by remember { mutableStateOf<String?>(null) }

    val uriHandler = LocalUriHandler.current

    LaunchedEffect(triggerUrl) {
        triggerUrl?.let {
            uriHandler.openUri(it)
            triggerUrl = null
        }
    }

    Text(
        text = text,
        color = if (url != null) PortfolioTheme.colors.linkText else PortfolioTheme.colors.secondaryText,
        fontSize = 13.sp,
        textAlign = TextAlign.Start,
        textDecoration = if (url != null) TextDecoration.Underline else TextDecoration.None,
        modifier = Modifier
            .clickable(enabled = url != null) {
                if (url != null) triggerUrl = url
            }
    )
}

