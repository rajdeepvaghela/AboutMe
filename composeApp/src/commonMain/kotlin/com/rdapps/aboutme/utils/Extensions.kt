package com.rdapps.aboutme.utils

import androidx.compose.ui.Modifier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

inline fun Modifier.doIf(
    condition: Boolean,
    ifFalse: Modifier.() -> Modifier = { this },
    ifTrue: Modifier.() -> Modifier
): Modifier = if (condition) this.ifTrue() else this.ifFalse()

inline fun <T> Modifier.doIfNotNull(obj: T?, block: Modifier.(obj: T) -> Modifier) =
    if (obj != null) this.block(obj) else this

@OptIn(ExperimentalSerializationApi::class)
fun createJson() = Json {
    isLenient = true
    ignoreUnknownKeys = true
    explicitNulls = false
    coerceInputValues = true
}

fun createHttpClient(json: Json = Json) = HttpClient {
    install(ContentNegotiation) {
        json(json)
    }
}