package com.rdapps.aboutme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rdapps.aboutme.theme.PortfolioTheme
import com.rdapps.aboutme.utils.LocalIsWideScreen
import kotlinx.coroutines.launch

private val CardShape = RoundedCornerShape(16.dp)
private val PillShape = RoundedCornerShape(20.dp)

data class WorkProject(
    val title: String,
    val subtitle: String,
    val context: String,
    val achievements: List<String>,
)

// Shared data models
data class WorkExperience(
    val company: String,
    val location: String,
    val roles: List<Role>
)

data class Role(
    val position: String,
    val period: String
)

data class SkillCategory(
    val category: String,
    val skills: List<String>
)

data class CompanyExperience(
    val work: WorkExperience,
    val projects: List<WorkProject>,
)

@Composable
fun AboutMe(modifier: Modifier = Modifier) {
    val isWideScreen = LocalIsWideScreen.current
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(vertical = 100.dp, horizontal = if (isWideScreen) 48.dp else 20.dp)
                .systemBarsPadding()
        ) {
            ExperienceSection()
            Spacer(modifier = Modifier.height(40.dp))

            EducationSection()
            Spacer(modifier = Modifier.height(40.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                ContactView()
            }
        }

        // Scroll to top button
        AnimatedVisibility(
            visible = scrollState.value > 0,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 80.dp)
                .systemBarsPadding()
        ) {
            SmallFloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        scrollState.animateScrollTo(0)
                    }
                },
                containerColor = PortfolioTheme.colors.accent,
                contentColor = PortfolioTheme.colors.background
            ) {
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowUp,
                    contentDescription = "Scroll to top"
                )
            }
        }
    }
}

@Composable
private fun ExperienceSection() {
    Text(
        text = "Experience",
        color = PortfolioTheme.colors.secondaryText,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 20.dp, bottom = 10.dp)
    )
    ExperienceContent()
}

@Composable
private fun ExperienceContent() {
    val isWideScreen = LocalIsWideScreen.current
    val companyExperiences = listOf(
        CompanyExperience(
            work = WorkExperience(
                "Red Elk Studios",
                "Hyderabad, India (Remote)",
                listOf(
                    Role(
                        "Senior Android Engineer",
                        "Aug 2024 - Present"
                    )
                )
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
                listOf(
                    Role(
                        "Senior Android Developer",
                        "Apr 2021 - Apr 2024"
                    )
                )
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
                listOf(
                    Role(
                        "Lead Android Developer",
                        "Oct 2020 - Mar 2021"
                    ),
                    Role(
                        "Android Developer",
                        "May 2018 - Oct 2020"
                    )
                )
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
                listOf(
                    Role(
                        "Android Developer",
                        "Dec 2016 - Apr 2018"
                    )
                )
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
                listOf(
                    Role(
                        "UI/UX Designer and Android Developer",
                        "Oct 2015 - May 2016"
                    )
                )
            ),
            projects = emptyList()
        )
    )

    var expandedCompany by remember { mutableStateOf<String?>(null) }

    if (isWideScreen) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            val list = companyExperiences.chunked(3)

            list.forEach {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    it.forEach { companyExperience ->
                        val isExpanded = expandedCompany == companyExperience.work.company
                        CompanyExperienceItem(
                            companyExperience = companyExperience,
                            isExpanded = isExpanded,
                            onToggle = {
                                expandedCompany =
                                    if (isExpanded) null else companyExperience.work.company
                            }
                        )
                    }
                }
            }
        }
    } else {
        companyExperiences.forEach { companyExperience ->
            val isExpanded = expandedCompany == companyExperience.work.company
            CompanyExperienceItem(
                companyExperience = companyExperience,
                isExpanded = isExpanded,
                onToggle = {
                    expandedCompany = if (isExpanded) null else companyExperience.work.company
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(PortfolioTheme.colors.secondaryBackground, CardShape)
                .padding(20.dp)
        ) {
            Text(
                text = category.category,
                color = PortfolioTheme.colors.primaryText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = category.skills.joinToString(separator = " / "),
                color = PortfolioTheme.colors.secondaryText,
                fontSize = 13.sp,
                lineHeight = 20.sp
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun EducationSection() {
    Text(
        text = "Education",
        color = PortfolioTheme.colors.secondaryText,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 20.dp, bottom = 10.dp)
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = PortfolioTheme.colors.secondaryBackground,
                shape = CardShape
            )
            .padding(20.dp)
    ) {
        Text(
            text = "B.E (Computer Science), 8.49 CGPA",
            color = PortfolioTheme.colors.primaryText,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Atmiya Institute of Technology and Science, Rajkot, Gujarat (2012 – 2016)",
            color = PortfolioTheme.colors.secondaryText,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun ContactSection() {
    val uriHandler = LocalUriHandler.current
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Rajdeep Vaghela",
            color = PortfolioTheme.colors.primaryText,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Android Developer",
            color = PortfolioTheme.colors.secondaryText,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))

        Spacer(modifier = Modifier.height(20.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            SocialPill("Github") { uriHandler.openUri("https://github.com/rajdeepvaghela") }
            SocialPill("LinkedIn") { uriHandler.openUri("https://linkedin.com/in/rajdeepvaghela") }
            SocialPill("E-mail") { uriHandler.openUri("mailto:rajdeep.vaghela610@gmail.com") }
            SocialPill("Play Store") { uriHandler.openUri("https://play.google.com/store/apps/developer?id=Rajdeep+Vaghela") }
        }
        Spacer(modifier = Modifier.height(12.dp))
        ContactItem(text = "+91 9033159066", url = null)
        ContactItem(text = "rajdeepvaghela.github.io", url = "https://rajdeepvaghela.github.io")
    }
}

@Composable
private fun SocialPill(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clickable { onClick() }
            .background(PortfolioTheme.colors.secondaryBackground, PillShape)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(label, color = PortfolioTheme.colors.primaryText, fontSize = 13.sp)
    }
}


@Composable
private fun CompanyExperienceItem(
    companyExperience: CompanyExperience,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(CardShape)
            .clickable(companyExperience.projects.isNotEmpty()) { onToggle() }
            .border(
                width = 1.dp,
                color = if (isExpanded) PortfolioTheme.colors.accentStroke else PortfolioTheme.colors.secondaryBackground,
                shape = CardShape
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
                FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = companyExperience.work.company,
                        color = PortfolioTheme.colors.primaryText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = companyExperience.work.location,
                        color = PortfolioTheme.colors.secondaryText,
                        fontSize = 13.sp
                    )
                }
                companyExperience.work.roles.forEach {
                    Text(
                        text = it.position,
                        color = PortfolioTheme.colors.secondaryText,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = it.period,
                        color = PortfolioTheme.colors.secondaryText,
                        fontSize = 12.sp
                    )
                }
            }
            if (companyExperience.projects.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Rounded.ArrowDropDown,
                    tint = PortfolioTheme.colors.secondaryText,
                    contentDescription = null,
                    modifier = Modifier.rotate(rotation)
                )
            }
        }

        AnimatedVisibility(
            visible = isExpanded && companyExperience.projects.isNotEmpty(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column {
                Spacer(modifier = Modifier.height(6.dp))
                HorizontalDivider(color = PortfolioTheme.colors.secondaryBackground)
                Spacer(modifier = Modifier.height(6.dp))
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