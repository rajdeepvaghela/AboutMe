package com.rdapps.aboutme.model

import kotlinx.serialization.Serializable


@Serializable
data class IpResponse(
    val ip: String = "",
    val city: String = "",
    val region: String = "",
    val country: String = "",
    val loc: String = "",
    val org: String = "",
    val postal: String = "",
    val timezone: String = ""
) {
    val lat: String = loc.split(",").firstOrNull() ?: ""

    val lng: String = loc.split(",").getOrNull(1) ?: ""
}