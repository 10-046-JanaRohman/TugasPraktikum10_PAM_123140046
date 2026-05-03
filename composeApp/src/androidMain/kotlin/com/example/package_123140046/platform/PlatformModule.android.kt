package com.example.package_123140046.di

import com.example.package_123140046.data.local.DatabaseDriverFactory
import com.example.package_123140046.data.local.SettingsFactory
import com.example.package_123140046.platform.DeviceInfo
import com.example.package_123140046.platform.NetworkMonitor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single { DatabaseDriverFactory(androidContext()) }
    single { SettingsFactory(androidContext()) }
    single { DeviceInfo(androidContext()) }
    single { NetworkMonitor(androidContext()) }
}
