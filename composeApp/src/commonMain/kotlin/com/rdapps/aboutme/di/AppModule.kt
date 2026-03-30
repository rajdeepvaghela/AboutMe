package com.rdapps.aboutme.di

import com.rdapps.aboutme.BuildKonfig
import com.rdapps.aboutme.deviceInfo.getDeviceInfo
import com.rdapps.aboutme.model.DeviceInfo
import com.rdapps.aboutme.utils.createHttpClient
import com.rdapps.aboutme.utils.createJson
import com.rdapps.aboutme.viewmodel.AppViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.serializer.KotlinXSerializer
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<Json> { createJson() }
    single<HttpClient> { createHttpClient(get()) }
    single<SupabaseClient> {
        createSupabaseClient(
            supabaseUrl = BuildKonfig.SUPABASE_URL,
            supabaseKey = BuildKonfig.SUPABASE_KEY
        ) {
            defaultSerializer = KotlinXSerializer(get())
            install(Postgrest)
        }
    }
    single<DeviceInfo> { getDeviceInfo() }
    viewModelOf(::AppViewModel)
}