package com.example.package_123140046.platform

import platform.Foundation.NSBundle
import platform.UIKit.UIDevice

actual class DeviceInfo {
    actual fun getDeviceName(): String = UIDevice.currentDevice.name

    actual fun getOsVersion(): String = "iOS ${UIDevice.currentDevice.systemVersion}"

    actual fun getAppVersion(): String {
        return NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleShortVersionString")?.toString() ?: "1.0"
    }
}
