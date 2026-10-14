package com.shilapi.xcertplay.compat

import android.annotation.TargetApi
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.Build

/**
 * API 18+ implementation of [BluetoothCompat.getAdapter].
 *
 * This class directly references android.bluetooth.BluetoothManager, so it
 * must never be loaded on API < 18. [BluetoothCompat] guards the call with a
 * SDK_INT check; the VM only resolves this class when that branch is taken.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
internal object BluetoothCompatApi18 {
    fun getAdapter(context: Context): BluetoothAdapter? {
        return (context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager)?.adapter
    }
}
