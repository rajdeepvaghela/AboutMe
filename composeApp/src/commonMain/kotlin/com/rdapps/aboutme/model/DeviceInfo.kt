package com.rdapps.aboutme.model

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class DeviceInfo(
    val userAgent: String = "",
    val vendor: String = "",
    val platform: String = "",
    val language: String = ""
)