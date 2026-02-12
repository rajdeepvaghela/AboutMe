package com.rdapps.aboutme

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

// Platform-specific URL opener used by the portfolio screen.
expect fun openUrl(url: String)