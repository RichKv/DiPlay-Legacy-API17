package com.shilapi.xcertplay.hud

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29], manifest = Config.NONE)
class CompatVehicleBoundaryTest {
    @Test fun enablingStoredVendorSettingsCannotStartVehicleOutput() {
        val context = RuntimeEnvironment.getApplication()
        BydOutputSettings.setEnabled(context, true)
        BydOutputSettings.setClusterStreamPause(context, true)
        BydOutputSettings.setBatteryToIphone(context, true)
        assertFalse(BydOutputSettings.enabled(context))
        assertFalse(BydOutputSettings.clusterStreamPause(context))
        assertFalse(BydOutputSettings.batteryToIphone(context))
        var callbackInvoked = false
        val callback: (Boolean) -> Unit = { callbackInvoked = true }
        BydNavigationOutputs.setClusterStreamControl(callback)
        BydNavigationOutputs.onAppOpened(context)
        BydNavigationOutputs.start(context)
        BydNavigationOutputs.setClusterMapShown(true)
        assertNull(BydNavigationOutputs.batteryStatus(context).snapshot())
        BydNavigationOutputs.endNow()
        BydNavigationOutputs.clearClusterStreamControl(callback)
        assertFalse(callbackInvoked)
    }
}
