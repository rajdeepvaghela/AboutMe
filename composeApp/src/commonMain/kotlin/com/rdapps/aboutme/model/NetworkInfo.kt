package com.rdapps.aboutme.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkInfo(
    @SerialName("user_id") val userId: String,
    val ip: String,
    val city: String,
    val region: String,
    val country: String,
    val lat: String,
    val lng: String,
    val org: String,
    val postal: String,
    val timezone: String
) {
    companion object {

        fun from(userId: String, ipResponse: IpResponse) = NetworkInfo(
            userId = userId,
            ip = ipResponse.ip,
            city = ipResponse.city,
            region = ipResponse.region,
            country = ipResponse.country,
            lat = ipResponse.lat,
            lng = ipResponse.lng,
            org = ipResponse.org,
            postal = ipResponse.postal,
            timezone = ipResponse.timezone
        )
    }
}
