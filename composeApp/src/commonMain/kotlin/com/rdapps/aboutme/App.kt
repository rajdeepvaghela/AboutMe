package com.rdapps.aboutme

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.rdapps.aboutme.theme.PortfolioTheme

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

    PortfolioTheme { isDark, onChange ->
        PortfolioScreen(
            isDark = isDark,
            onToggleTheme = {
                onChange(!isDark)
            }
        )
    }
}