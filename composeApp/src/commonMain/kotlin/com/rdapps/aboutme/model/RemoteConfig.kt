package com.rdapps.aboutme.model

import kotlinx.serialization.Serializable


@Serializable
data class RemoteConfig(
    val ipinfoToken: String = ""
)