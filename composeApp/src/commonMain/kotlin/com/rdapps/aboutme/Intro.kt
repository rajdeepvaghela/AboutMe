package com.rdapps.aboutme

import aboutme.composeapp.generated.resources.Res
import aboutme.composeapp.generated.resources.hi_i_am
import aboutme.composeapp.generated.resources.ic_email
import aboutme.composeapp.generated.resources.ic_github
import aboutme.composeapp.generated.resources.ic_linkedin
import aboutme.composeapp.generated.resources.ic_playstore
import aboutme.composeapp.generated.resources.label_email
import aboutme.composeapp.generated.resources.label_github
import aboutme.composeapp.generated.resources.label_linkedin
import aboutme.composeapp.generated.resources.label_play_store
import aboutme.composeapp.generated.resources.name_first
import aboutme.composeapp.generated.resources.name_last
import aboutme.composeapp.generated.resources.title_senior_android_engineer
import org.jetbrains.compose.resources.stringResource
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rdapps.aboutme.theme.PortfolioTheme
import com.rdapps.aboutme.utils.LocalIsWideScreen
import com.rdapps.aboutme.viewmodel.AppViewModel
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Preview
@Composable
fun IntroSection(onEvent: (PortfolioScreenEvent) -> Unit = {}, modifier: Modifier = Modifier) {
    val isWideScreen = LocalIsWideScreen.current

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
                        text = stringResource(Res.string.hi_i_am),
                        color = PortfolioTheme.colors.secondaryText,
                        fontSize = if (isWideScreen) 18.sp else 14.sp,
                        fontWeight = FontWeight.Medium
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
                    text = stringResource(Res.string.name_first).uppercase(),
                    color = PortfolioTheme.colors.primaryText,
                    fontSize = firstNameSize,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = stringResource(Res.string.name_last).uppercase(),
                    color = PortfolioTheme.colors.primaryText.copy(alpha = 0.35f),
                    fontSize = lastNameSize,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Subtitle in accent color, spaced letters
                Text(
                    text = stringResource(Res.string.title_senior_android_engineer).uppercase(),
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
                    .padding(end = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ContactView(onEvent)
            }
        }
    }
}

@Composable
private fun SocialCircleIcon(
    resource: DrawableResource,
    contentDescription: String,
    size: Dp = 48.dp,
    iconSize: Dp = 20.dp,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .border(1.dp, PortfolioTheme.colors.secondaryText.copy(alpha = 0.5f), CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(resource),
            contentDescription = contentDescription,
            tint = PortfolioTheme.colors.primaryText,
            modifier = Modifier.size(iconSize)
        )
    }
}

@Composable
fun ContactView(onEvent: (PortfolioScreenEvent) -> Unit) {
    val uriHandler = LocalUriHandler.current
    SocialCircleIcon(
        resource = Res.drawable.ic_linkedin,
        contentDescription = stringResource(Res.string.label_linkedin),
        onClick = {
            onEvent(PortfolioScreenEvent.TrackEvent(AppViewModel.Events.ClickLinkedIn))
            uriHandler.openUri("https://linkedin.com/in/rajdeepvaghela")
        }
    )
    SocialCircleIcon(
        resource = Res.drawable.ic_email,
        contentDescription = stringResource(Res.string.label_email),
        onClick = {
            onEvent(PortfolioScreenEvent.TrackEvent(AppViewModel.Events.ClickEmail))
            uriHandler.openUri("mailto:rajdeep.vaghela610@gmail.com")
        }
    )
    SocialCircleIcon(
        resource = Res.drawable.ic_playstore,
        contentDescription = stringResource(Res.string.label_play_store),
        onClick = {
            onEvent(PortfolioScreenEvent.TrackEvent(AppViewModel.Events.ClickPlayStore))
            uriHandler.openUri("https://play.google.com/store/apps/dev?id=4737354144616321734")
        }
    )
    SocialCircleIcon(
        resource = Res.drawable.ic_github,
        contentDescription = stringResource(Res.string.label_github),
        onClick = {
            onEvent(PortfolioScreenEvent.TrackEvent(AppViewModel.Events.ClickGithub))
            uriHandler.openUri("https://github.com/rajdeepvaghela")
        }
    )
}