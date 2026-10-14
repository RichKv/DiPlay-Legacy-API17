package com.shilapi.xcertplay.hud

import android.content.Context
import com.shilapi.xcertplay.iap2.wire.Iap2Frame
import com.shilapi.xcertplay.transport.VehicleStatusProvider

/** API17 vehicle boundary: no vendor objects, workers, ADB, I2C, HUD or cluster initialization. */
object BydNavigationOutputs {
    private val noVehicleStatus = VehicleStatusProvider { null }
    fun onAppOpened(context: Context) = Unit
    fun setDiagnosticHold(hold: Boolean) = Unit
    fun setClusterMapShown(shown: Boolean) = Unit
    fun setClusterStreamControl(control: (Boolean) -> Unit) = Unit
    fun clearClusterStreamControl(control: (Boolean) -> Unit) = Unit
    fun batteryStatus(context: Context): VehicleStatusProvider = noVehicleStatus
    fun start(context: Context) = Unit
    internal fun onFrame(frame: Iap2Frame) = Unit
    fun endNow() = Unit
}
