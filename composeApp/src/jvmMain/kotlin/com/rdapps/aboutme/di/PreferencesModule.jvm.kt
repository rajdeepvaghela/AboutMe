package com.rdapps.aboutme.di

import com.rdapps.aboutme.preferences.Preferences
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.io.files.Path
import org.koin.core.module.Module
import org.koin.dsl.module
import java.io.File

actual val preferencesModule: Module = module {
    single<KStore<Preferences>> {
        val appDir = File(System.getProperty("user.home") + "/.aboutme")
        appDir.mkdirs()
        storeOf(file = Path(appDir.absolutePath + "/preferences.json"))
    }
}