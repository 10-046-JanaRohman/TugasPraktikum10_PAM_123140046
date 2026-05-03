package com.example.package_123140046.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.package_123140046.platform.NetworkMonitor
import org.koin.compose.koinInject

@Composable
fun NetworkStatusIndicator() {
    val networkMonitor: NetworkMonitor = koinInject()
    val isConnected by networkMonitor.observeConnectivity().collectAsState(initial = networkMonitor.isConnected())

    AnimatedVisibility(visible = !isConnected) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.errorContainer
        ) {
            Text(
                text = "Tidak ada koneksi internet - data tetap tersedia dari database lokal",
                modifier = Modifier.padding(12.dp),
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}
