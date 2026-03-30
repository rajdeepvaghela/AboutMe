package com.rdapps.aboutme.di

import com.rdapps.aboutme.preferences.Preferences
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.storage.storeOf
import org.koin.core.module.Module
import org.koin.dsl.module

actual val preferencesModule: Module = module {
    single<KStore<Preferences>> {
        storeOf(key = "preferences")
    }
}