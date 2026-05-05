package com.rdapps.aboutme.di

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.rdapps.aboutme.preferences.Preferences
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.createGraphFactory
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.io.files.Path

@DependencyGraph(AppScope::class)
interface AndroidAppGraph : AppGraph {
    @Provides
    fun providePreferencesStore(context: Context): KStore<Preferences> {
        val path = context.filesDir.absolutePath + "/preferences.json"
        return storeOf(file = Path(path))
    }

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@Provides context: Context): AndroidAppGraph
    }
}

@Composable
actual fun rememberAppGraph(): AppGraph {
    val context = LocalContext.current.applicationContext
    return remember(context) {
        createGraphFactory<AndroidAppGraph.Factory>().create(context)
    }
}
