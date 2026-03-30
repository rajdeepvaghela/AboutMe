package com.rdapps.aboutme.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rdapps.aboutme.BuildKonfig
import com.rdapps.aboutme.model.DeviceInfo
import com.rdapps.aboutme.model.Event
import com.rdapps.aboutme.model.IpResponse
import com.rdapps.aboutme.model.NetworkInfo
import com.rdapps.aboutme.model.RemoteConfig
import com.rdapps.aboutme.model.User
import com.rdapps.aboutme.preferences.Preferences
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import io.github.jan.supabase.postgrest.postgrest
import io.github.xxfast.kstore.KStore
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.request.get
import kotlinx.coroutines.launch

class AppViewModel(
    private val deviceInfo: DeviceInfo,
    private val preferencesStore: KStore<Preferences>,
    private val client: HttpClient,
    private val supabase: SupabaseClient
) : ViewModel() {

    private var userId: String? = null

    init {
        printInDebug("DeviceInfo: $deviceInfo")
        fetchDataAndTrack()
    }

    private fun fetchDataAndTrack() = viewModelScope.launch {
        userId = getOrCreateUserId(deviceInfo)
        printInDebug("UserId: $userId")

        val config = fetchRemoteConfig() ?: return@launch
        val ipResponse = fetchIpInfo(config.ipinfoToken) ?: return@launch
        trackNetworkInfo(ipResponse)
        trackEventNow(Events.Visit)
    }

    private suspend fun getOrCreateUserId(
        deviceInfo: DeviceInfo,
        forceCreate: Boolean = false
    ): String? {
        if (!forceCreate) {
            preferencesStore.get()?.userId?.let { userId ->
                return userId
            }
        }

        val payload = User.from(deviceInfo)
        printInDebug("User: $payload")
        val user = supabase.postgrest.from("users")
            .insert(payload) { select() }
            .decodeSingle<User>()

        preferencesStore.set(Preferences(userId = user.id ?: ""))
        return user.id
    }

    private suspend fun trackNetworkInfo(ipResponse: IpResponse) {
        val userId = userId ?: return

        suspend fun track(userId: String) {
            val networkInfo = NetworkInfo.from(userId, ipResponse)
            supabase.postgrest.from("user_network_info").upsert(networkInfo)
        }

        try {
            track(userId)
        } catch (e: PostgrestRestException) {
            if (e.code == "23503") { // when userId doesn't exists
                val userId = getOrCreateUserId(deviceInfo, forceCreate = true) ?: return
                this.userId = userId
                track(userId)
            } else {
                e.printStackTrace()
            }
        } catch (e: HttpRequestTimeoutException) {
            e.printStackTrace()
        } catch (e: HttpRequestException) {
            e.printStackTrace()
        }
    }

    private suspend fun trackEventNow(event: Events) {
        val userId = userId ?: return
        try {
            supabase.postgrest.from("events")
                .insert(
                    Event(
                        userId = userId,
                        name = when (event) {
                            is Events.ClickLivePreview -> {
                                "${event.name}_${event.project}"
                            }

                            is Events.ClickStopPreview -> {
                                "${event.name}_${event.project}"
                            }

                            is Events.OpenLink -> {
                                "${event.name}_${event.project}"
                            }

                            is Events.OpenWeddingInvitation -> {
                                "${event.name}_${event.enteredName}"
                            }

                            else -> {
                                event.name
                            }
                        }
                    )
                )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (event is Events.OpenWeddingInvitation) {
            updateName(userId, event.enteredName)
        }
    }

    fun trackEvent(event: Events) = viewModelScope.launch {
        printInDebug("Event: $event")
        trackEventNow(event)
    }

    private fun updateName(userId: String, name: String) = viewModelScope.launch {
        try {
            supabase.postgrest.from("users")
                .update({ User::name setTo name }) {
                    filter { User::id eq userId }
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun fetchRemoteConfig(): RemoteConfig? {
        try {
            return supabase.postgrest.rpc("get_remote_config").decodeAsOrNull<RemoteConfig>().also {
                printInDebug("RemoteConfig: $it")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private suspend fun fetchIpInfo(ipInfoToken: String): IpResponse? {
        try {
            return client.get("https://ipinfo.io/?token=$ipInfoToken").body<IpResponse>().also {
                printInDebug("IpResponse: $it")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun printInDebug(msg: String) {
        if (!BuildKonfig.DEBUG) return
        println(msg)
    }

    sealed class Events(val name: String) {
        data object Visit : Events("Visit")
        data class OpenWeddingInvitation(val enteredName: String) : Events("OpenWeddingInvitation")
        data object ToggleTheme : Events("ToggleTheme")
        data object OpenProjects : Events("OpenProjects")
        data object OpenAboutMe : Events("OpenAboutMe")
        data object ClickLinkedIn : Events("ClickLinkedIn")
        data object ClickGithub : Events("ClickGithub")
        data object ClickEmail : Events("ClickEmail")
        data object ClickPlayStore : Events("ClickPlayStore")
        data class ClickStopPreview(val project: String) : Events("ClickStopPreview")
        data class ClickLivePreview(val project: String) : Events("ClickLivePreview")
        data class OpenLink(val project: String) : Events("OpenLink")
        data object ClickShowMore : Events("ClickShowMore")
        data object ClickShowLess : Events("ClickShowLess")
        data object ClickScrollToTop : Events("ClickScrollToTop")
        data object ExpandExperience : Events("ExpandExperience")
    }
}