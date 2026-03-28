package com.rdapps.aboutme.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Event(
    @SerialName("user_id") val userId: String,
    val name: String
)
