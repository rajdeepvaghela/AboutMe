package com.rdapps.aboutme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
private fun WorkSection(isWideScreen: Boolean) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Work",
            color = PortfolioTheme.colors.primaryText,
            fontSize = if (isWideScreen) 120.sp else 72.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.End)
        )

        Spacer(modifier = Modifier.height(48.dp))

        WorkEntry("2024 -", "Present", "Red Elk Studios", "Senior Android Engineer |\nHyderabad (Remote)")
        WorkEntry("2020 - 2024", "4 years", "Holofy", "Senior Android Engineer |\nSouth Bank, England (Remote)", isHighlighted = true)
        WorkEntry("2018 - 2020", "2 years", "NoBroker.com", "Senior Android Engineer |\nBangalore, India")
        WorkEntry("2016 - 2018", "1.5 years", "Vyapar Tech Solutions", "Android Engineer |\nBangalore, India")
        WorkEntry("2015 - 2016", "1 year", "Axaram Codelabs", "Android and UI/UX Designer |\nRajkot, Gujarat")

        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = buildAnnotatedString {
                append("Work experience\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = PortfolioTheme.colors.primaryText)) {
                    append("9+ years")
                }
            },
            color = PortfolioTheme.colors.secondaryText,
            fontSize = 16.sp,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth(),
            lineHeight = 24.sp
        )
    }
}

@Composable
private fun WorkEntry(
    period: String,
    duration: String,
    company: String,
    role: String,
    isHighlighted: Boolean = false
) {
    Surface(
        color = if (isHighlighted) PortfolioTheme.colors.primaryText else Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        val textColor = if (isHighlighted) PortfolioTheme.colors.background else PortfolioTheme.colors.primaryText
        val subTextColor = if (isHighlighted) PortfolioTheme.colors.secondaryText else PortfolioTheme.colors.secondaryText

        Row(
            modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(0.3f)) {
                Text(text = period, color = textColor, fontSize = 14.sp)
                Text(text = duration, color = subTextColor, fontSize = 13.sp)
            }
            Column(modifier = Modifier.weight(0.7f)) {
                Text(text = company, color = textColor, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = role, color = textColor, fontSize = 16.sp, lineHeight = 24.sp)
            }
        }
    }
}

@Preview(device = Devices.DESKTOP)
@Composable
fun WorkSectionWidePreview() {
    Surface(color = PortfolioTheme.colors.background) {
        Box(modifier = Modifier.padding(24.dp)) {
            WorkSection(isWideScreen = true)
        }
    }
}

@Preview
@Composable
fun WorkSectionMobilePreview() {
    Surface(color = PortfolioTheme.colors.background) {
        Box(modifier = Modifier.padding(24.dp)) {
            WorkSection(isWideScreen = false)
        }
    }
}
