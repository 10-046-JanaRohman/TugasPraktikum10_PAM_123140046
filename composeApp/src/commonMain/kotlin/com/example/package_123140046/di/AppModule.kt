package com.example.package_123140046.di

import com.example.package_123140046.ai.AIRepository
import com.example.package_123140046.ai.AIRepositoryImpl
import com.example.package_123140046.ai.GeminiService
import com.example.package_123140046.data.local.DatabaseProvider
import com.example.package_123140046.data.repository.NoteRepository
import com.example.package_123140046.data.repository.SettingsRepository
import com.example.package_123140046.viewmodel.ChatViewModel
import com.example.package_123140046.viewmodel.NotesViewModel
import com.example.package_123140046.viewmodel.SettingsViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

val commonModule = module {
    single {
        HttpClient {
            install(HttpTimeout) {
                requestTimeoutMillis = 30_000
                connectTimeoutMillis = 30_000
                socketTimeoutMillis = 30_000
            }
        }
    }

    single { DatabaseProvider(get()).database }

    single { NoteRepository(get()) }
    single { SettingsRepository(get()) }

    single { GeminiService(get()) }
    single<AIRepository> { AIRepositoryImpl(get()) }

    single { NotesViewModel(get(), get()) }
    single { SettingsViewModel(get()) }
    single { ChatViewModel(get()) }
}

expect fun platformModule(): Module

fun appModules(): List<Module> {
    return listOf(commonModule, platformModule())
}

fun initKoin() {
    startKoin {
        modules(appModules())
    }
}