package com.rdapps.aboutme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.rdapps.aboutme.di.rememberAppGraph
import com.rdapps.aboutme.theme.PortfolioTheme
import com.rdapps.aboutme.viewmodel.AppViewModel

@Composable
@Preview
fun App() {
    val appGraph = rememberAppGraph()

    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components {
                add(KtorNetworkFetcherFactory())
            }
            .build()
    }

    val viewModel: AppViewModel = remember(appGraph) { appGraph.appViewModel }
    val isSystemInDarkTheme = isSystemInDarkTheme()
    var isDarkTheme by remember { mutableStateOf(isSystemInDarkTheme) }

    PortfolioTheme(isDarkTheme) {
        PortfolioScreen(
            isDark = isDarkTheme,
            onToggleTheme = {
                isDarkTheme = !isDarkTheme
                viewModel.trackEvent(AppViewModel.Events.ToggleTheme)
            },
            onEvent = {
                when (it) {
                    is PortfolioScreenEvent.TrackEvent -> viewModel.trackEvent(it.event)
                }
            }
        )
    }
}
