package com.rdapps.aboutme.di

import android.content.Context
import com.rdapps.aboutme.preferences.Preferences
import io.github.xxfast.kstore.file.storeOf
import kotlinx.io.files.Path
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton

@Module
actual class PreferencesModule {
    @Singleton
    fun getPreferences(context: Context) =
        storeOf<Preferences>(file = Path(context.filesDir.absolutePath + "/preferences.json"))
}