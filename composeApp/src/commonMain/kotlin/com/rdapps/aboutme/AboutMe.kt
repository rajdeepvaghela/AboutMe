package com.rdapps.aboutme

import aboutme.composeapp.generated.resources.Res
import aboutme.composeapp.generated.resources.ic_email
import aboutme.composeapp.generated.resources.ic_github
import aboutme.composeapp.generated.resources.ic_linkedin
import aboutme.composeapp.generated.resources.ic_playstore
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource

private val CardShape = RoundedCornerShape(16.dp)
private val ButtonShape = RoundedCornerShape(24.dp)
private val PillShape = RoundedCornerShape(20.dp)

@Composable
fun AboutMe(isWideScreen: Boolean, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = if (isWideScreen) 48.dp else 20.dp)
            .padding(top = 100.dp, bottom = 100.dp)
    ) {
        ExperienceSection()
        Spacer(modifier = Modifier.height(40.dp))

        EducationSection()
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Preview
@Composable
fun AboutSection(isWideScreen: Boolean = false, modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxSize().weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(
                        start = if (isWideScreen) 72.dp else 24.dp,
                        end = 24.dp
                    )
            ) {
                // "Hi I am ————————" row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Hi I am",
                        color = PortfolioTheme.colors.secondaryText,
                        fontSize = if (isWideScreen) 18.sp else 14.sp,
                        fontWeight = FontWeight.Normal
                    )
                    HorizontalDivider(
                        modifier = Modifier.width(if (isWideScreen) 420.dp else 180.dp),
                        color = PortfolioTheme.colors.accent,
                        thickness = 1.5.dp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Large name
                val firstNameSize = if (isWideScreen) 100.sp else 50.sp
                val lastNameSize = if (isWideScreen) 100.sp else 50.sp

                Text(
                    text = "Rajdeep".uppercase(),
                    color = PortfolioTheme.colors.primaryText,
                    fontSize = firstNameSize,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Vaghela".uppercase(),
                    color = PortfolioTheme.colors.primaryText.copy(alpha = 0.35f),
                    fontSize = lastNameSize,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Subtitle in accent color, spaced letters
                Text(
                    text = "SENIOR ANDROID ENGINEER",
                    color = PortfolioTheme.colors.accent,
                    fontSize = if (isWideScreen) 16.sp else 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 3.sp,
                    modifier = Modifier.align(Alignment.End)
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = if (isWideScreen) 36.dp else 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SocialCircleIcon(
                    painter = painterResource(Res.drawable.ic_github),
                    contentDescription = "GitHub",
                    onClick = { uriHandler.openUri("https://github.com/rajdeepvaghela") }
                )
                SocialCircleIcon(
                    painter = painterResource(Res.drawable.ic_linkedin),
                    contentDescription = "LinkedIn",
                    onClick = { uriHandler.openUri("https://linkedin.com/in/rajdeepvaghela") }
                )
                SocialCircleIcon(
                    painter = painterResource(Res.drawable.ic_email),
                    contentDescription = "E-mail",
                    onClick = { uriHandler.openUri("mailto:rajdeep.vaghela610@gmail.com") }
                )
                SocialCircleIcon(
                    painter = painterResource(Res.drawable.ic_playstore),
                    contentDescription = "Play Store",
                    onClick = { uriHandler.openUri("https://play.google.com/store/apps/dev?id=4737354144616321734") }
                )
            }
        }
    }
}

@Composable
private fun SocialCircleIcon(
    painter: androidx.compose.ui.graphics.painter.Painter,
    contentDescription: String,
    size: Dp = 48.dp,
    iconSize: Dp = 20.dp,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(size)
            .border(1.dp, PortfolioTheme.colors.secondaryText.copy(alpha = 0.5f), CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            tint = PortfolioTheme.colors.primaryText,
            modifier = Modifier.size(iconSize)
        )
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
                "UI/UX Designer and Android Developer",
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
        Spacer(modifier = Modifier.height(12.dp))
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
) {
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f
    )

    Column(
        modifier = Modifier
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