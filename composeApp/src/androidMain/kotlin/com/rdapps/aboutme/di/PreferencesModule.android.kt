package com.rdapps.aboutme.di

import com.rdapps.aboutme.preferences.Preferences
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.io.files.Path
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val preferencesModule: Module = module {
    single<KStore<Preferences>> {
        val context = androidContext()
        val path = context.filesDir.absolutePath + "/preferences.json"
        storeOf(file = Path(path))
    }
}