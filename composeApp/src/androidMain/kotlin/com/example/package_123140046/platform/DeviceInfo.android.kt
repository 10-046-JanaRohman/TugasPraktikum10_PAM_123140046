package com.example.package_123140046.platform

import android.content.Context
import android.os.Build

actual class DeviceInfo(private val context: Context) {
    actual fun getDeviceName(): String = "${Build.MANUFACTURER} ${Build.MODEL}".trim()

    actual fun getOsVersion(): String = "Android ${Build.VERSION.RELEASE} (SDK ${Build.VERSION.SDK_INT})"

    actual fun getAppVersion(): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName ?: "1.0"
        } catch (_: Exception) {
            "1.0"
        }
    }
}
