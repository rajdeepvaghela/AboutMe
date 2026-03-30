package com.rdapps.aboutme

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.rdapps.aboutme.di.appModule
import com.rdapps.aboutme.di.preferencesModule
import com.rdapps.aboutme.theme.PortfolioTheme
import com.rdapps.aboutme.viewmodel.AppViewModel
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel
import org.koin.dsl.koinConfiguration

@Composable
@Preview
fun App() {
    KoinApplication(configuration = koinConfiguration { modules(appModule, preferencesModule) }) {
        setSingletonImageLoaderFactory { context ->
            ImageLoader.Builder(context)
                .components {
                    add(KtorNetworkFetcherFactory())
                }
                .build()
        }

        val viewModel: AppViewModel = koinViewModel()

        PortfolioTheme { isDark, onChange ->
            PortfolioScreen(
                isDark = isDark,
                onToggleTheme = {
                    onChange(!isDark)
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
}