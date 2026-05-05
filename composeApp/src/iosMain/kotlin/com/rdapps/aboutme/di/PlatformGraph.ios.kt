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
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.io.files.Path
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

@DependencyGraph(AppScope::class)
interface IosAppGraph : AppGraph {
    @OptIn(ExperimentalForeignApi::class)
    @Provides
    fun providePreferencesStore(): KStore<Preferences> {
        val fileManager: NSFileManager = NSFileManager.defaultManager
        val documentsUrl: NSURL = fileManager.URLForDirectory(
            directory = NSDocumentDirectory,
            appropriateForURL = null,
            create = false,
            inDomain = NSUserDomainMask,
            error = null
        )!!
        val path = "${documentsUrl.path}/preferences.json"
        return storeOf(file = Path(path))
    }
}

@Composable
actual fun rememberAppGraph(): AppGraph = remember { createGraph<IosAppGraph>() }
