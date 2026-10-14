package com.shilapi.xcertplay.compat

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.os.Build

/**
 * API-level-safe access to the local [BluetoothAdapter].
 *
 * android.bluetooth.BluetoothManager only exists on API 18+. Referencing it
 * directly (even via `as?`) from code that runs on API 17 risks a
 * VerifyError/NoClassDefFoundError at class-load time, because Dalvik must
 * resolve the type. This entry point never mentions BluetoothManager itself;
 * the API 18 implementation lives in [BluetoothCompatApi18], which the VM
 * only loads when [Build.VERSION.SDK_INT] >= 18.
 */
object BluetoothCompat {
    fun getAdapter(context: Context): BluetoothAdapter? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            BluetoothCompatApi18.getAdapter(context)
        } else {
            // API 17 and below: the pre-JELLY_BEAN_MR2 way to get the adapter.
            @Suppress("DEPRECATION")
            BluetoothAdapter.getDefaultAdapter()
        }
    }
}
