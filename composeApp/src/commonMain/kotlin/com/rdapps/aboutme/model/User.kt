package com.rdapps.aboutme.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String? = null,
    @SerialName("user_agent") val userAgent: String,
    val vendor: String,
    val platform: String,
    val language: String,
    val name: String? = null
) {
    companion object {
        fun from(deviceInfo: DeviceInfo) = User(
            id = null,
            userAgent = deviceInfo.userAgent,
            vendor = deviceInfo.vendor,
            platform = deviceInfo.platform,
            language = deviceInfo.language
        )
    }
}
