package com.rdapps.aboutme.di

import com.rdapps.aboutme.deviceInfo.getDeviceInfo
import com.rdapps.aboutme.model.DeviceInfo
import com.rdapps.aboutme.utils.createHttpClient
import com.rdapps.aboutme.utils.createJson
import com.rdapps.aboutme.utils.createSupabase
import com.rdapps.aboutme.viewmodel.AppViewModel
import io.github.jan.supabase.SupabaseClient
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create
import org.koin.plugin.module.dsl.viewModel

val appModule = module {
    includes(preferencesModule)
    single<Json> { create(::createJson) }
    single<HttpClient> { create(::createHttpClient) }
    single<SupabaseClient> { create(::createSupabase) }
    single<DeviceInfo> { create(::getDeviceInfo) }
    viewModel<AppViewModel>()
}