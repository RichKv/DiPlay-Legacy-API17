package com.shilapi.xcertplay

import com.shilapi.xcertplay.orchestration.MfiTarget
import com.shilapi.xcertplay.orchestration.WirelessHotspotMode
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

/** The compatibility edition preserves upstream preference choices. */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29], manifest = Config.NONE)
class CompatPreferencesTest {
    @Test fun compatibleChoicesRemainUserConfigurable() {
        val context = RuntimeEnvironment.getApplication()
        context.getSharedPreferences("xcertplay_airplay", 0).edit()
            .putBoolean("wireless_enabled", false).putBoolean("hevc_enabled", true)
            .putBoolean("hevc_software_decoder", true).putInt("display_fps", 60)
            .putString("wireless_hotspot_mode", "WIFI_P2P")
            .putInt("navigation_stream_type", 14).putString("mfi_target", "USB_CH341").apply()
        assertFalse(AirPlayPersistence.loadWirelessEnabled(context))
        assertTrue(AirPlayPersistence.loadHevcEnabled(context))
        assertTrue(AirPlayPersistence.loadHevcSoftwareDecoderEnabled(context))
        assertEquals(60, AirPlayPersistence.loadFps(context))
        assertEquals(14, AirPlayPersistence.loadNavigationStreamType(context))
        assertEquals(WirelessHotspotMode.WIFI_P2P, AirPlayPersistence.loadWirelessHotspotMode(context))
        assertEquals(MfiTarget.USB_CH341, AirPlayPersistence.loadMfiTarget(context))
    }
}
