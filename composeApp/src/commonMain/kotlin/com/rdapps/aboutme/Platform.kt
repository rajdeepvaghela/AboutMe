package com.rdapps.aboutme

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform