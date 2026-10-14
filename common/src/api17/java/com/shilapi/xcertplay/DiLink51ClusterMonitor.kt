package com.shilapi.xcertplay

import android.content.Context

/** API17 has no DiLink cluster or Usage Access; never allocate a polling worker. */
internal class DiLink51ClusterMonitor(context: Context, onState: (ClusterActivityState.Snapshot) -> Unit) {
    fun start() = Unit
    fun stop() = Unit
    companion object {
        fun hasAccess(context: Context): Boolean = false
    }
}
