package com.example.package_123140046.platform

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

actual class NetworkMonitor {
    actual fun isConnected(): Boolean = true
    actual fun observeConnectivity(): Flow<Boolean> = flowOf(true)
}
