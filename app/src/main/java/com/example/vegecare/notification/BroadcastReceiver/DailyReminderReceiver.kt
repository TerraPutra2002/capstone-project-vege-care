package com.example.vegecare.notification.BroadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.vegecare.notification.Helper.DailyReminderNotificationHelper

class DailyReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        DailyReminderNotificationHelper.createDailyReminderNotification(
            context,
            "Pengingat Perawatan Harian",
            "Jangan lupa untuk merawat tanaman Anda hari ini!"
        )
    }
}