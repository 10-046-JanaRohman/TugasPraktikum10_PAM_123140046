package com.example.package_123140046.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.package_123140046.data.model.AppTheme
import com.example.package_123140046.data.model.SortOrder
import com.example.package_123140046.platform.DeviceInfo
import com.example.package_123140046.viewmodel.SettingsViewModel
import org.koin.compose.koinInject

@Composable
fun ProfileScreen() {
    val settingsViewModel: SettingsViewModel = koinInject()
    val deviceInfo: DeviceInfo = koinInject()

    val appTheme by settingsViewModel.appTheme.collectAsState()
    val sortOrder by settingsViewModel.sortOrder.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Profile & Settings",
            style = MaterialTheme.typography.headlineSmall
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Device Info",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(text = "Device: ${deviceInfo.getDeviceName()}")
                Text(text = "OS: ${deviceInfo.getOsVersion()}")
                Text(text = "App Version: ${deviceInfo.getAppVersion()}")
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                ListItem(
                    headlineContent = {
                        Text("Theme")
                    },
                    supportingContent = {
                        Text("Pilih tema aplikasi")
                    }
                )

                ThemeOptionItem(
                    title = "System",
                    description = "Mengikuti tema perangkat",
                    selected = appTheme == AppTheme.SYSTEM,
                    onClick = {
                        settingsViewModel.setAppTheme(AppTheme.SYSTEM)
                    }
                )

                ThemeOptionItem(
                    title = "Light",
                    description = "Selalu gunakan tema terang",
                    selected = appTheme == AppTheme.LIGHT,
                    onClick = {
                        settingsViewModel.setAppTheme(AppTheme.LIGHT)
                    }
                )

                ThemeOptionItem(
                    title = "Dark",
                    description = "Selalu gunakan tema gelap",
                    selected = appTheme == AppTheme.DARK,
                    onClick = {
                        settingsViewModel.setAppTheme(AppTheme.DARK)
                    }
                )

                HorizontalDivider()

                ListItem(
                    headlineContent = {
                        Text("Sort Order")
                    },
                    supportingContent = {
                        Text(
                            if (sortOrder == SortOrder.NEWEST) {
                                "Catatan terbaru ditampilkan lebih dulu"
                            } else {
                                "Catatan terlama ditampilkan lebih dulu"
                            }
                        )
                    },
                    trailingContent = {
                        Switch(
                            checked = sortOrder == SortOrder.NEWEST,
                            onCheckedChange = { checked ->
                                settingsViewModel.setSortOrder(
                                    if (checked) SortOrder.NEWEST else SortOrder.OLDEST
                                )
                            }
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun ThemeOptionItem(
    title: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = {
            Text(title)
        },
        supportingContent = {
            Text(description)
        },
        trailingContent = {
            RadioButton(
                selected = selected,
                onClick = onClick
            )
        }
    )
}