package com.rdapps.aboutme

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.rdapps.aboutme.deviceInfo.getDeviceInfo
import com.rdapps.aboutme.preferences.createPreferencesStore
import com.rdapps.aboutme.theme.PortfolioTheme
import com.rdapps.aboutme.viewmodel.AppViewModel

val appViewModelFactory = viewModelFactory {
    initializer {
        AppViewModel(deviceInfo = getDeviceInfo(), preferencesStore = createPreferencesStore())
    }
}

@Composable
@Preview
fun App() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components {
                add(KtorNetworkFetcherFactory())
            }
            .build()
    }

    val viewModel: AppViewModel = viewModel(factory = appViewModelFactory)

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