package com.rdapps.aboutme

import aboutme.composeapp.generated.resources.Res
import aboutme.composeapp.generated.resources.label_email
import aboutme.composeapp.generated.resources.label_github
import aboutme.composeapp.generated.resources.label_linkedin
import aboutme.composeapp.generated.resources.label_play_store
import aboutme.composeapp.generated.resources.label_scroll_top
import aboutme.composeapp.generated.resources.section_education
import aboutme.composeapp.generated.resources.section_experience
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material3.FloatingActionButtonDefaults
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rdapps.aboutme.theme.PortfolioTheme
import com.rdapps.aboutme.utils.LocalIsWideScreen
import com.rdapps.aboutme.viewmodel.AppViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

private val CardShape = RoundedCornerShape(16.dp)
private val PillShape = RoundedCornerShape(20.dp)

data class WorkProject(
    val title: String,
    val subtitle: String,
    val link: String? = null,
    val achievements: List<AnnotatedString>,
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
fun AboutMe(onEvent: (PortfolioScreenEvent) -> Unit, modifier: Modifier = Modifier) {
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
            ExperienceSection(onEvent)
            Spacer(modifier = Modifier.height(40.dp))

            EducationSection()
            Spacer(modifier = Modifier.height(40.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                ContactView(onEvent)
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
                    onEvent(PortfolioScreenEvent.TrackEvent(AppViewModel.Events.ClickScrollToTop))
                    coroutineScope.launch {
                        scrollState.animateScrollTo(0)
                    }
                },
                containerColor = PortfolioTheme.colors.accent,
                contentColor = PortfolioTheme.colors.background,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowUp,
                    contentDescription = stringResource(Res.string.label_scroll_top)
                )
            }
        }
    }
}

@Composable
private fun ExperienceSection(onEvent: (PortfolioScreenEvent) -> Unit) {
    Text(
        text = stringResource(Res.string.section_experience),
        color = PortfolioTheme.colors.secondaryText,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 20.dp, bottom = 10.dp)
    )
    ExperienceContent(onEvent)
}

@Composable
private fun ExperienceContent(onEvent: (PortfolioScreenEvent) -> Unit) {
    val isWideScreen = LocalIsWideScreen.current
    val companyExperiences = listOf(
        CompanyExperience(
            work = WorkExperience(
                "Red Elk Studios",
                "Hyderabad, India (Remote)",
                listOf(
                    Role(
                        "Senior Mobile Engineer - || (Android)",
                        "Aug 2024 - Apr 2026"
                    )
                )
            ),
            projects = listOf(
                WorkProject(
                    title = "IMVU - Social Chat & Avatar application",
                    subtitle = "Client at Red Elk Studios",
                    link = "https://play.google.com/store/apps/details?id=com.imvu.mobilecordova",
                    achievements = listOf(
                        "Led modernization using <sb>Kotlin Flows</sb> and <sb>Jetpack Compose</sb> UI components.".toAnnotated(),
                        "Refactored legacy <sb>Socket connection</sb> architecture to OkHttp.".toAnnotated(),
                        "Migrated core features into <sb>MVI / MVVM architecture</sb> with Compose.".toAnnotated(),
                        "Delivered new features and maintained existing experiences."
                    ).toAnnotatedString()
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
                    title = "Holofy Spaces - Mobile and Android TV",
                    subtitle = "Saas platform to add interactive videos to websites.",
                    achievements = listOf(
                        "Implemented video compression pipelines using <sb>FFmpeg</sb> and <sb>MediaCodec.</sb>".toAnnotated(),
                        "Designed and built a reusable Compose UI Kit.",
                        "Integrated 100ms <sb>live streaming</sb> SDK with low-latency playback.".toAnnotated(),
                        "Shipped an <sb>Android TV</sb> module optimized for large screens.".toAnnotated()
                    ).toAnnotatedString()
                ),
                WorkProject(
                    title = "Pro Caller, Pro Emailer & Pro Scheduler",
                    subtitle = "AI-powered utility applications",
                    achievements = listOf(
                        "Architected <sb>Single-Activity</sb> apps fully in <sb>Jetpack Compose.</sb>".toAnnotated(),
                        "Integrated Twilio SDK for communication features.",
                        "Designed a cohesive <sb>design system</sb> with theme support.".toAnnotated()
                    ).toAnnotatedString()
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
                    title = "NoBroker Property Rent & Sale",
                    subtitle = "Real estate and home services-based application.",
                    link = "https://play.google.com/store/apps/details?id=com.nobroker.app",
                    achievements = listOf(
                        "Owned the Android app and led the team for a brief period.",
                        "Developed a <sb>Hybrid WebView</sb> with custom caching for <sb>Dynamic Delivery</sb>, forming the foundation for many future features.".toAnnotated(),
                        "Introduced and migrated core features from Java to Kotlin.",
                        "Improved <sb>crash-free</sb> user rate to <sb>99.4%.</sb>".toAnnotated(),
                        "Integrated <sb>JusPay, RazorPay</sb>, and <sb>Paytm SDKs</sb> for payments, <sb>Truecaller SDK</sb> for sign up / sign in, and <sb>Adobe, Firebase</sb>, and <sb>Facebook SDKs</sb> for analytics.".toAnnotated()
                    ).toAnnotatedString()
                ),
                WorkProject(
                    title = "Part Time Jobs, Work From Home",
                    subtitle = "Earn money by submitting pictures of To-Let board or providing details of the Owner who wants to Rent their property.",
                    link = "https://play.google.com/store/apps/details?id=com.nobroker.clickearn",
                    achievements = emptyList()
                ),
                WorkProject(
                    title = "Packers & Movers by NoBroker",
                    subtitle = "Comprehensive logistics app for local and interstate relocation, including vehicle transport.",
                    link = "https://play.google.com/store/apps/details?id=com.nobroker.packersmovers",
                    achievements = emptyList()
                ),
                WorkProject(
                    title = "Pay rent with Credit Cards",
                    subtitle = "A secure digital platform that allows users to pay rent, security deposits, society maintenance, and tuition fees using credit cards, debit cards, or UPI while earning rewards and cashback.",
                    link = "https://play.google.com/store/apps/details?id=com.nobroker.pay",
                    achievements = emptyList()
                ),
                WorkProject(
                    title = "NoBroker Painting & Cleaning",
                    subtitle = "Convenient, one-stop platform for booking reliable professional services like cleaning, painting, pest control, and interior design.",
                    link = "https://play.google.com/store/apps/details?id=com.nobroker.homeservices",
                    achievements = emptyList()
                ),
                WorkProject(
                    title = "NoBrokerHood:Smart Society App",
                    subtitle = "Comprehensive visitor and society management app designed to streamline security, accounting, and community living for residential and commercial complexes.",
                    link = "https://play.google.com/store/apps/details?id=com.app.nobrokerhood",
                    achievements = listOf(
                        "Maintained and developed the application for short period of time.",
                        "Developed a <sb>Hybrid WebView</sb> with custom caching for <sb>Dynamic Delivery</sb>, forming the foundation for many future features.".toAnnotated(),
                        "Integrated <sb>Truecaller SDK</sb> for sign up / sign in".toAnnotated()
                    ).toAnnotatedString()
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
                    title = "Vyapar - Billing App GST Invoice Maker",
                    subtitle = "Billing and GST invoicing application.",
                    link = "https://play.google.com/store/apps/details?id=in.android.vyapar",
                    achievements = listOf(
                        "Integrated hardware components like <sb>POS machines</sb>, <sb>Thermal Printers</sb> and <sb>USB Barcode Scanners.</sb>".toAnnotated(),
                        "Developed <sb>plugin applications</sb> to reduce the size of the main app.".toAnnotated(),
                        "Revamped the overall app design using <sb>Material Components.</sb>".toAnnotated(),
                        "Integrated SDKs such as CleverTap, iTextPdf, and Branch.io."
                    ).toAnnotatedString()
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
                    if (!isExpanded) {
                        onEvent(PortfolioScreenEvent.TrackEvent(AppViewModel.Events.ExpandExperience))
                    }
                    expandedCompany = if (isExpanded) null else companyExperience.work.company
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

private fun List<CharSequence>.toAnnotatedString() = map {
    when (it) {
        is AnnotatedString -> it
        is String -> AnnotatedString(it)
        else -> AnnotatedString(it.toString())
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
        text = stringResource(Res.string.section_education),
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
            SocialPill(stringResource(Res.string.label_github)) { uriHandler.openUri("https://github.com/rajdeepvaghela") }
            SocialPill(stringResource(Res.string.label_linkedin)) { uriHandler.openUri("https://linkedin.com/in/rajdeepvaghela") }
            SocialPill(stringResource(Res.string.label_email)) { uriHandler.openUri("mailto:rajdeep.vaghela610@gmail.com") }
            SocialPill(stringResource(Res.string.label_play_store)) { uriHandler.openUri("https://play.google.com/store/apps/developer?id=Rajdeep+Vaghela") }
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

        val uriHandler = LocalUriHandler.current

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
                    val interactionSource = remember(project) { MutableInteractionSource() }
                    val isHovered by interactionSource.collectIsHoveredAsState()

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable(
                                enabled = project.link != null,
                                interactionSource = interactionSource
                            ) {
                                project.link?.let { uriHandler.openUri(it) }
                            }
                    ) {
                        Text(
                            text = project.title,
                            color = PortfolioTheme.colors.primaryText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            textDecoration = if (isHovered && project.link != null) TextDecoration.Underline else null
                        )
                        if (project.link != null) {
                            Icon(
                                imageVector = Icons.Rounded.Link,
                                tint = PortfolioTheme.colors.linkText,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(start = 4.dp)
                                    .size(16.dp)
                            )
                        }
                    }
                    Text(
                        text = project.subtitle,
                        color = PortfolioTheme.colors.secondaryText,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    project.achievements.forEach { achievement ->
                        Row {
                            Spacer(
                                Modifier
                                    .padding(top = 10.dp, end = 4.dp)
                                    .size(4.dp)
                                    .background(PortfolioTheme.colors.primaryText, CircleShape)
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