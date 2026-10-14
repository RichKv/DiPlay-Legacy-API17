package com.shilapi.xcertplay.network

import android.os.Build
import java.net.Socket

/** Detect a vanished peer, without treating a legitimately idle CarPlay screen as a failure. */
internal object TcpLiveness {
    fun configure(socket: Socket, diagnostic: (String) -> Unit) {
        socket.keepAlive = true
        // Before 29, fromSocket owns the original descriptor rather than a duplicate.
        // The API 29+ tuning (android.system.Os) is isolated in TcpLivenessApi29
        // so this class verifies cleanly on API 17.
        if (Build.VERSION.SDK_INT < 29) return
        TcpLivenessApi29.configure(socket, diagnostic)
    }
}
