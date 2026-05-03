package com.example.package_123140046

import androidx.compose.ui.window.ComposeUIViewController
import com.example.package_123140046.di.initKoin
import org.koin.core.context.GlobalContext

fun MainViewController() = ComposeUIViewController {
    if (GlobalContext.getOrNull() == null) {
        initKoin()
    }
    App()
}
