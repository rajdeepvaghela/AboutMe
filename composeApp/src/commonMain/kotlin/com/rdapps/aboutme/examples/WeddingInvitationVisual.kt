package com.rdapps.aboutme.examples

import aboutme.composeapp.generated.resources.Res
import aboutme.composeapp.generated.resources.wedding_invitation_preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rdapps.aboutme.theme.PortfolioTheme
import com.rdapps.aboutme.utils.LocalIsWideScreen
import org.jetbrains.compose.resources.painterResource
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
private fun encodeToBase64(text: String): String = Base64.encode(text.encodeToByteArray())

@Composable
fun WeddingInvitationVisual(modifier: Modifier) {
    Box(
        modifier = modifier.fillMaxSize().clip(RoundedCornerShape(40.dp))
    ) {
        Image(
            painter = painterResource(Res.drawable.wedding_invitation_preview),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier.fillMaxWidth()
                .height(if (LocalIsWideScreen.current) 300.dp else 250.dp)
        )

        var name by remember { mutableStateOf("") }
        val uriHandler = LocalUriHandler.current

        Column(
            modifier = Modifier
                .matchParentSize()
                .background(PortfolioTheme.colors.background.copy(alpha = 0.8f)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Your Name") },
                placeholder = { Text("e.g. John Doe") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = PortfolioTheme.colors.primaryText,
                    unfocusedTextColor = PortfolioTheme.colors.primaryText,
                    focusedBorderColor = PortfolioTheme.colors.accent,
                    unfocusedBorderColor = PortfolioTheme.colors.secondaryText,
                    focusedLabelColor = PortfolioTheme.colors.accent,
                    unfocusedLabelColor = PortfolioTheme.colors.secondaryText,
                    focusedPlaceholderColor = PortfolioTheme.colors.secondaryText,
                    unfocusedPlaceholderColor = PortfolioTheme.colors.secondaryText,
                    cursorColor = PortfolioTheme.colors.accent,
                ),
                modifier = Modifier.padding(16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .alpha(if (name.isNotBlank()) 1f else 0.5f)
                    .clickable(enabled = name.isNotBlank()) {
                        val json =
                            """{"name":"$name","showContactNumber":true,"showReceptionDetails":true,"showStayDetails":true}"""
                        val encoded = encodeToBase64(json)
                        uriHandler.openUri("https://rajdeepvaghela.github.io/WeddingInvitation/#$encoded")
                    }
            ) {
                Icon(
                    imageVector = Icons.Rounded.PlayCircle,
                    contentDescription = "Play",
                    tint = PortfolioTheme.colors.primaryText,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "Live Preview",
                    fontWeight = FontWeight.Medium,
                    color = PortfolioTheme.colors.primaryText
                )
            }
        }
    }
}