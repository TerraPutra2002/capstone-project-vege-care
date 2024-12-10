package com.example.vegecare.notification.Helper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.vegecare.R
import com.example.vegecare.notification.NotificationItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object WeatherNotificationHelper {
    private const val CHANNEL_ID = "weather_notification_channel"
    private const val CHANNEL_NAME = "Weather Notifications"
    private const val PREFS_NAME = "weather_notifications"
    private const val NOTIFICATIONS_KEY = "notifications"

    fun createNotification(context: Context, title: String, message: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_cloud_24)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .build()

        val notificationId = System.currentTimeMillis().toInt()
        notificationManager.notify(notificationId, notification)

        val currentNotifications = getNotifications(context).toMutableList()
        currentNotifications.add(
            NotificationItem(notificationId, title, message, System.currentTimeMillis())
        )
        saveNotifications(context, currentNotifications)
    }

    fun saveNotifications(context: Context, notifications: List<NotificationItem>) {
        val currentNotifications = DailyReminderNotificationHelper.getNotifications(context).toMutableList()
        val newNotifications = notifications.filterNot { newItem ->
            currentNotifications.any { it.id == newItem.id }
        }
        currentNotifications.addAll(newNotifications)
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = Gson().toJson(notifications)
        sharedPreferences.edit().putString(NOTIFICATIONS_KEY, json).apply()
    }

    fun getNotifications(context: Context): List<NotificationItem> {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(NOTIFICATIONS_KEY, "[]")
        val type = object : TypeToken<List<NotificationItem>>() {}.type
        return Gson().fromJson(json, type)
    }
}
