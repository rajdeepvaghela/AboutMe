package com.rdapps.aboutme.preferences

import com.rdapps.aboutme.AppContextHolder
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.io.files.Path

actual fun createPreferencesStore(): KStore<Preferences> {
    val path = AppContextHolder.appContext.filesDir.absolutePath + "/preferences.json"
    return storeOf(file = Path(path))
}


