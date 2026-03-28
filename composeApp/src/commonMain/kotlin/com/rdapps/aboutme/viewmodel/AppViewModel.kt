package com.rdapps.aboutme.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rdapps.aboutme.Config
import com.rdapps.aboutme.model.DeviceInfo
import com.rdapps.aboutme.model.Event
import com.rdapps.aboutme.model.IpResponse
import com.rdapps.aboutme.model.NetworkInfo
import com.rdapps.aboutme.model.RemoteConfig
import com.rdapps.aboutme.model.User
import com.rdapps.aboutme.preferences.Preferences
import com.rdapps.aboutme.utils.createHttpClient
import com.rdapps.aboutme.utils.createJson
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.xxfast.kstore.KStore
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel(
    private val deviceInfo: DeviceInfo,
    private val preferencesStore: KStore<Preferences>
) : ViewModel() {
    private val supabase by lazy {
        createSupabaseClient(
            supabaseUrl = Config.SUPABASE_URL,
            supabaseKey = Config.SUPABASE_KEY
        ) {
            install(Postgrest)
        }
    }

    private val client by lazy {
        createHttpClient(createJson())
    }

    data class UiState(
        val userId: String? = null
    )

    val uiState: StateFlow<UiState>
        field = MutableStateFlow(UiState())

    init {
        print("DeviceInfo: $deviceInfo")
        fetchData()

        viewModelScope.launch {
            preferencesStore.updates.collect { pref ->
                pref ?: return@collect
            }
        }
    }

    private fun fetchData() = viewModelScope.launch {
        val userId = getOrCreateUserId(deviceInfo)
        print("UserId: $userId")
        uiState.update { it.copy(userId = userId) }

        val config = fetchRemoteConfig() ?: return@launch
        val ipResponse = fetchIpInfo(config.ipinfoToken) ?: return@launch
        trackNetworkInfo(userId, ipResponse)
        trackEvent(Events.Visit, userId)
    }

    private suspend fun getOrCreateUserId(deviceInfo: DeviceInfo): String {
        preferencesStore.get()?.userId?.let { userId ->
            return userId
        }

        val payload = User.from(deviceInfo)
        val user = supabase.postgrest.from("users")
            .insert(payload) { select() }
            .decodeSingle<User>()

        preferencesStore.set(Preferences(userId = user.id ?: ""))

        return user.id ?: ""
    }

    private fun trackNetworkInfo(userId: String, ipResponse: IpResponse) = viewModelScope.launch {
        val networkInfo = NetworkInfo.from(userId, ipResponse)
        supabase.postgrest.from("user_network_info").upsert(networkInfo)
    }

    fun trackEvent(event: Events, userId: String? = uiState.value.userId) =
        viewModelScope.launch {
            userId ?: return@launch
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

            if (event is Events.OpenWeddingInvitation) {
                updateName(userId, event.enteredName)
            }
        }

    private fun updateName(userId: String, name: String) = viewModelScope.launch {
        supabase.postgrest.from("users")
            .update(
                {
                    User::name setTo name
                }
            ) {
                filter {
                    User::id eq userId
                }
            }
    }

    private suspend fun fetchRemoteConfig(): RemoteConfig? {
        try {
            return supabase.postgrest.rpc("get_remote_config").decodeAsOrNull<RemoteConfig>().also {
                print("RemoteConfig: $it")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private suspend fun fetchIpInfo(ipInfoToken: String): IpResponse? {
        try {
            return client.get("https://ipinfo.io/?token=$ipInfoToken").body<IpResponse>().also {
                print("IpResponse: $it")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
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