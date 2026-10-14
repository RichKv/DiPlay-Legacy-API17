package com.shilapi.xcertplay

import android.app.Instrumentation
import android.content.Intent
import android.os.Bundle
import android.os.Build
import com.shilapi.xcertplay.compat.BluetoothCompat
import com.shilapi.xcertplay.compat.RuntimeCapabilities
import com.shilapi.xcertplay.hud.BydNavigationOutputs
import java.io.Closeable

/** Dalvik checks for installation/startup compatibility, without authentication or ROOT. */
class Api17SmokeChecks {
    fun run(instrumentation: Instrumentation) {
        val context = instrumentation.targetContext
        val result = Bundle()
        val completed = mutableListOf<String>()
        try {
            check(Build.VERSION.SDK_INT == 17) { "Run this smoke suite on API17" }
            check(RuntimeCapabilities.api17 && !RuntimeCapabilities.nativeSupport)
            completed += "API17 source-set capabilities"
            val classes = listOf(
                "com.shilapi.xcertplay.DiPlayActivity", "com.shilapi.xcertplay.CarPlayHostActivity",
                "com.shilapi.xcertplay.DiPlaySessionService", "com.shilapi.xcertplay.media.AndroidMediaSink",
                "com.shilapi.xcertplay.media.AudioRenderer", "com.shilapi.xcertplay.airplay.AirPlaySession",
                "com.shilapi.xcertplay.orchestration.CarPlayController", "com.shilapi.xcertplay.network.ManualHotspotManager",
                "com.shilapi.xcertplay.transport.Iap2CsmChannel", "com.shilapi.xcertplay.transport.Iap2LinkChannel",
                "com.shilapi.xcertplay.transport.Iap2LocationProvider", "com.shilapi.xcertplay.iap2.session.Iap2Session"
            )
            for (name in classes) {
                val type = Class.forName(name, false, context.classLoader)
                type.declaredMethods; type.declaredConstructors; type.declaredFields
                completed += "Class descriptors: $name"
            }
            check(Closeable::class.java.isAssignableFrom(Class.forName("com.shilapi.xcertplay.transport.Iap2LinkChannel")))
            BluetoothCompat.getAdapter(context)
            completed += "Bluetooth API17 fallback does not require permission grants"
            val helpers = Class.forName("com.shilapi.xcertplay.airplay.AirPlaySessionKt")
            val socket = java.net.Socket()
            helpers.getMethod("safeClose", java.net.Socket::class.java).invoke(null, socket)
            check(socket.isClosed)
            val server = java.net.ServerSocket()
            helpers.getMethod("safeClose", java.net.ServerSocket::class.java).invoke(null, server)
            check(server.isClosed)
            completed += "Socket/ServerSocket close without Closeable cast"
            BydNavigationOutputs.onAppOpened(context)
            BydNavigationOutputs.start(context)
            check(BydNavigationOutputs.batteryStatus(context).snapshot() == null)
            BydNavigationOutputs.endNow()
            completed += "No vendor worker initialization"
            try {
                com.shilapi.xcertplay.transport.LinuxI2cTransport.open("/dev/i2c-1")
                error("Native I2C opened in the API17 source-only variant")
            } catch (expected: IllegalStateException) {
                check(expected.message == "Native I2C is unavailable in API17")
            }
            completed += "Native I2C rejected before library load"
            val interfaces = java.net.NetworkInterface.getNetworkInterfaces()
            while (interfaces != null && interfaces.hasMoreElements()) {
                check(com.shilapi.xcertplay.compat.NetworkInterfaceCompat.index(interfaces.nextElement()) >= 0)
            }
            completed += "NetworkInterface index fallback on API17"
            // Preference storage retains the upstream connection and display choices.
            context.getSharedPreferences("xcertplay_airplay", 0).edit()
                .putBoolean("wireless_enabled", false).putInt("display_fps", 60)
                .putString("mfi_target", "USB_CH341").commit()
            check(!AirPlayPersistence.loadWirelessEnabled(context))
            check(AirPlayPersistence.loadFps(context) == 60)
            check(AirPlayPersistence.loadMfiTarget(context) == com.shilapi.xcertplay.orchestration.MfiTarget.USB_CH341)
            completed += "Upstream connection and display preferences remain configurable"
            val activity = instrumentation.startActivitySync(
                Intent(context, DiPlayActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            instrumentation.waitForIdleSync()
            val authority = context.packageName + ".diagnostics"
            check(context.packageManager.resolveContentProvider(authority, 0)?.packageName == context.packageName)
            val directory = java.io.File(context.filesDir, "diagnostics")
            val existing = directory.listFiles()?.map { it.name }?.toSet() ?: emptySet()
            instrumentation.runOnMainSync {
                activity.javaClass.getDeclaredMethod("exportDiagnostics", android.net.Uri::class.java).apply {
                    isAccessible = true
                }.invoke(activity, null)
            }
            val deadline = android.os.SystemClock.uptimeMillis() + 15_000
            var exported: java.io.File? = null
            while (android.os.SystemClock.uptimeMillis() < deadline && exported == null) {
                exported = directory.listFiles()?.firstOrNull { it.name !in existing && it.length() > 0 }
                if (exported == null) Thread.sleep(100)
            }
            check(exported != null && exported!!.readText().startsWith("DiPlay "))
            completed += "API17 diagnostic file export and scoped content provider"
            instrumentation.runOnMainSync { check(!activity.isFinishing); activity.finish() }
            completed += "Main screen starts without ROOT or accessory identity"
            result.putString("checks", completed.joinToString("\n"))
            result.putInt("check_count", completed.size)
            instrumentation.finish(-1, result)
        } catch (failure: Throwable) {
            result.putString("completed", completed.joinToString("\n"))
            result.putString("failure", android.util.Log.getStackTraceString(failure))
            instrumentation.finish(0, result)
        }
    }
}
