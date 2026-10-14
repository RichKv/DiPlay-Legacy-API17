package com.shilapi.xcertplay.network

import android.annotation.TargetApi
import android.os.Build
import android.os.ParcelFileDescriptor
import android.system.Os
import android.system.OsConstants
import java.net.Socket

/**
 * API 29+ TCP keepalive tuning.
 *
 * android.system.Os only exists on API 21+, and
 * ParcelFileDescriptor.fromSocket(Socket) on API 29+. This class directly
 * references them, so it must never be loaded on API < 29. [TcpLiveness]
 * guards the call with a SDK_INT check.
 */
@TargetApi(29)
internal object TcpLivenessApi29 {
    // Linux UAPI <linux/tcp.h>; Android's SDK omits these three OsConstants fields.
    private const val TCP_KEEPIDLE = 4
    private const val TCP_KEEPINTVL = 5
    private const val TCP_KEEPCNT = 6

    fun configure(socket: Socket, diagnostic: (String) -> Unit) {
        try {
            ParcelFileDescriptor.fromSocket(socket).use { duplicate ->
                val fd = duplicate.fileDescriptor
                Os.setsockoptInt(fd, OsConstants.IPPROTO_TCP, TCP_KEEPIDLE, 10)
                Os.setsockoptInt(fd, OsConstants.IPPROTO_TCP, TCP_KEEPINTVL, 3)
                Os.setsockoptInt(fd, OsConstants.IPPROTO_TCP, TCP_KEEPCNT, 3)
                Os.setsockoptInt(fd, OsConstants.IPPROTO_TCP, OsConstants.TCP_USER_TIMEOUT, 20_000)
                diagnostic("TCP peer health enabled idle=10s interval=3s count=3 deadline=20000ms; no video-idle timeout")
            }
        } catch (failure: Exception) {
            diagnostic("TCP peer health tuning unavailable: ${failure.javaClass.simpleName}")
        }
    }
}
