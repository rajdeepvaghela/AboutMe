package com.rdapps.aboutme.di

import com.rdapps.aboutme.deviceInfo.getDeviceInfo
import com.rdapps.aboutme.model.DeviceInfo
import com.rdapps.aboutme.utils.createHttpClient
import com.rdapps.aboutme.utils.createJson
import com.rdapps.aboutme.utils.createSupabaseClient
import io.github.jan.supabase.SupabaseClient
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton

@Module(includes = [PreferencesModule::class])
@ComponentScan("com.rdapps.aboutme.viewmodel")
class AppModule {

    @Singleton
    fun json(): Json = createJson()

    @Singleton
    fun httpClient(json: Json): HttpClient = createHttpClient(json)

    @Singleton
    fun supabaseClient(json: Json): SupabaseClient = createSupabaseClient(json)

    @Singleton
    fun deviceInfo(): DeviceInfo = getDeviceInfo()
}