package com.rdapps.aboutme.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.rdapps.aboutme.preferences.Preferences
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.createGraph
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.io.files.Path
import java.io.File

@DependencyGraph(AppScope::class)
interface JvmAppGraph : AppGraph {
    @Provides
    fun providePreferencesStore(): KStore<Preferences> {
        val appDir = File(System.getProperty("user.home") + "/.aboutme")
        appDir.mkdirs()
        return storeOf(file = Path(appDir.absolutePath + "/preferences.json"))
    }
}

@Composable
actual fun rememberAppGraph(): AppGraph = remember { createGraph<JvmAppGraph>() }
