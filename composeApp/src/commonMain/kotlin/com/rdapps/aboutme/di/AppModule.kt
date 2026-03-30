package com.rdapps.aboutme.di

import com.rdapps.aboutme.deviceInfo.getDeviceInfo
import com.rdapps.aboutme.model.DeviceInfo
import com.rdapps.aboutme.viewmodel.AppViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<DeviceInfo> { getDeviceInfo() }
    viewModelOf(::AppViewModel)
}