package com.example.vegecare.notification.Helper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.vegecare.R

object TemperatureNotificationHelper {

    private const val CHANNEL_ID = "temperature_notification_channel"
    private const val CHANNEL_NAME = "Temperature Notifications"
    private const val CHANNEL_DESCRIPTION = "Notifications related to temperature alerts"

    /**
     * Membuat dan menampilkan notifikasi terkait suhu.
     * @param context Context aplikasi.
     * @param title Judul notifikasi.
     * @param message Pesan notifikasi.
     */
    fun createTemperatureNotification(context: Context, title: String, message: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Membuat NotificationChannel untuk API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Membuat notifikasi
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_flare_24)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Untuk API < 26
            .setAutoCancel(true)
            .build()

        // Menampilkan notifikasi
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}

