package com.rdapps.aboutme.preferences

import kotlinx.serialization.Serializable

@Serializable
data class Preferences(
    val userId: String = ""
)

