package com.shilapi.xcertplay.network

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.net.wifi.SoftApConfiguration
import android.net.wifi.WifiManager
import android.os.Build
import com.shilapi.xcertplay.transport.Iap2WirelessSecurity

/**
 * API 30+ reader for the system SoftAP configuration.
 *
 * android.net.wifi.SoftApConfiguration only exists on API 30+. This class
 * directly references it, so it must never be loaded on API < 30.
 * [ManualHotspotManager] guards the call with a SDK_INT check.
 */
@TargetApi(Build.VERSION_CODES.R)
internal object SoftApConfigReaderApi30 {
    @SuppressLint("PrivateApi")
    fun read(wifiManager: WifiManager): ManualHotspotManager.ManualApConfiguration? {
        return try {
            val method = WifiManager::class.java.getMethod("getSoftApConfiguration")
            val configuration = method.invoke(wifiManager) as? SoftApConfiguration
                ?: return null
            val ssid = configuration.ssid ?: return null
            val bandAndChannel = when {
                Build.VERSION.SDK_INT >= 36 -> {
                    val channels = configuration.channels
                    if (channels.size() == 0) null else channels.keyAt(0) to channels.valueAt(0)
                }
                else -> {
                    val band = (
                        SoftApConfiguration::class.java
                            .getMethod("getBand")
                            .invoke(configuration) as? Number
                        )?.toInt()
                    val channel = SoftApConfiguration::class.java
                        .getMethod("getChannel")
                        .invoke(configuration) as? Number
                    if (band == null || channel == null) null else band to channel.toInt()
                }
            }
            val band = bandAndChannel?.first
            val channel = bandAndChannel?.second ?: 0
            ManualHotspotManager.ManualApConfiguration(
                ssid = ssid,
                band = band,
                channel = channel,
                frequencyMHz = wifiChannelToFrequencyMhz(channel, band),
                security = mapSecurity(configuration.securityType),
            )
        } catch (_: Throwable) {
            null
        }
    }

    private fun mapSecurity(securityType: Int): Iap2WirelessSecurity = when (securityType) {
        SoftApConfiguration.SECURITY_TYPE_OPEN -> Iap2WirelessSecurity.NONE
        SoftApConfiguration.SECURITY_TYPE_WPA2_PSK -> Iap2WirelessSecurity.WPA_WPA2
        SoftApConfiguration.SECURITY_TYPE_WPA3_SAE_TRANSITION ->
            Iap2WirelessSecurity.WPA3_TRANSITION
        SoftApConfiguration.SECURITY_TYPE_WPA3_SAE -> Iap2WirelessSecurity.WPA3_ONLY
        else -> Iap2WirelessSecurity.WPA_WPA2
    }
}
