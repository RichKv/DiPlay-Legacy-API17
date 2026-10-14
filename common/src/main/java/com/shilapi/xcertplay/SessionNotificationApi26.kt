package com.shilapi.xcertplay

import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.annotation.RequiresApi

@RequiresApi(26)
internal object SessionNotificationApi26 {
    fun createChannel(manager: NotificationManager, name: String) {
        manager.createNotificationChannel(NotificationChannel(name, "CarPlay connection", NotificationManager.IMPORTANCE_LOW))
    }
}
