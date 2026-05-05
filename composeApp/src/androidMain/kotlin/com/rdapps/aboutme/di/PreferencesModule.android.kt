package com.rdapps.aboutme.di

import android.content.Context
import com.rdapps.aboutme.preferences.Preferences
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.io.files.Path
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create

actual val preferencesModule: Module = module {
    single<KStore<Preferences>> { create(::createPreferences) }
}

private fun createPreferences(context: Context): KStore<Preferences> {
    val path = context.filesDir.absolutePath + "/preferences.json"
    return storeOf(file = Path(path))
}