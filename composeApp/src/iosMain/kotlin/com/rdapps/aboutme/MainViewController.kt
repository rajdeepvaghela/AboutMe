package com.rdapps.aboutme

import androidx.compose.ui.window.ComposeUIViewController
import com.rdapps.aboutme.di.appModule
import com.rdapps.aboutme.di.preferencesModule
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController {
    App()
}

fun initKoin() {
    startKoin {
        modules(appModule, preferencesModule)
    }
}
