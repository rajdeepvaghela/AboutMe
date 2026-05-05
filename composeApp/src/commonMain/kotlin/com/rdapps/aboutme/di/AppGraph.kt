package com.rdapps.aboutme.di

import com.rdapps.aboutme.BuildKonfig
import com.rdapps.aboutme.deviceInfo.getDeviceInfo
import com.rdapps.aboutme.model.DeviceInfo
import com.rdapps.aboutme.utils.createHttpClient
import com.rdapps.aboutme.utils.createJson
import com.rdapps.aboutme.viewmodel.AppViewModel
import dev.zacsweers.metro.Provides
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.serializer.KotlinXSerializer
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json


interface AppGraph {
    val appViewModel: AppViewModel

    @Provides
    fun provideJson(): Json = createJson()

    @Provides
    fun provideHttpClient(json: Json): HttpClient = createHttpClient(json)

    @Provides
    fun provideSupabaseClient(json: Json): SupabaseClient =
        createSupabaseClient(
            supabaseUrl = BuildKonfig.SUPABASE_URL,
            supabaseKey = BuildKonfig.SUPABASE_KEY
        ) {
            defaultSerializer = KotlinXSerializer(json)
            install(Postgrest)
        }

    @Provides
    fun provideDeviceInfo(): DeviceInfo = getDeviceInfo()
}
