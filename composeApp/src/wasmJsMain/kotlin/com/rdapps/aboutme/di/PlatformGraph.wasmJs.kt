package com.rdapps.aboutme.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.rdapps.aboutme.preferences.Preferences
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.createGraph
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.storage.storeOf

@DependencyGraph(AppScope::class)
interface WasmJsAppGraph : AppGraph {
    @Provides
    fun providePreferencesStore(): KStore<Preferences> = storeOf(key = "preferences")
}

@Composable
actual fun rememberAppGraph(): AppGraph = remember { createGraph<WasmJsAppGraph>() }
