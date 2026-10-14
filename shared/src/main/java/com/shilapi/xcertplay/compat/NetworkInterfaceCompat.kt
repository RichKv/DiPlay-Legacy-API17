package com.shilapi.xcertplay.compat

import android.os.Build
import java.net.Inet6Address
import java.net.NetworkInterface

object NetworkInterfaceCompat {
    fun index(networkInterface: NetworkInterface): Int =
        if (Build.VERSION.SDK_INT >= 19) NetworkInterfaceApi19.index(networkInterface)
        else {
            // API17 can use an already-scoped IPv6 address; IPv4 needs no interface index.
            val addresses = networkInterface.inetAddresses
            var scope = 0
            while (addresses.hasMoreElements() && scope <= 0) {
                val address = addresses.nextElement()
                if (address is Inet6Address) scope = address.scopeId
            }
            scope
        }
}

@android.annotation.TargetApi(19)
private object NetworkInterfaceApi19 {
    fun index(networkInterface: NetworkInterface): Int = networkInterface.index
}
