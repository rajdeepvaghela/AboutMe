package com.rdapps.aboutme.di

import com.rdapps.aboutme.preferences.Preferences
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.io.files.Path
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton
import java.io.File

@Module
actual class PreferencesModule {
    @Singleton
    fun getPreferences(): KStore<Preferences> {
        val appDir = File(System.getProperty("user.home") + "/.aboutme")
        appDir.mkdirs()
        return storeOf(file = Path(appDir.absolutePath + "/preferences.json"))
    }
}