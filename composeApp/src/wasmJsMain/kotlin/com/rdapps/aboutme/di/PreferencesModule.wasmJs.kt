package com.rdapps.aboutme.di

import com.rdapps.aboutme.preferences.Preferences
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.storage.storeOf
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton

@Module
actual class PreferencesModule {
    @Singleton
    fun getPreferences(): KStore<Preferences> = storeOf(key = "preferences")
}