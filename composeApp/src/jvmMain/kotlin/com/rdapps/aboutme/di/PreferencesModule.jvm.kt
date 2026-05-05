package com.rdapps.aboutme.di

import com.rdapps.aboutme.preferences.Preferences
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.io.files.Path
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create
import java.io.File

actual val preferencesModule: Module = module {
    single<KStore<Preferences>> {
        create(::createPreferences)
    }
}

private fun createPreferences(): KStore<Preferences> {
    val appDir = File(System.getProperty("user.home") + "/.aboutme")
    appDir.mkdirs()
    return storeOf(file = Path(appDir.absolutePath + "/preferences.json"))
}