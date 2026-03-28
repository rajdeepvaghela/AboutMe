package com.rdapps.aboutme.preferences

import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.storage.storeOf

actual fun createPreferencesStore(): KStore<Preferences> {
    return storeOf(key = "preferences")
}

