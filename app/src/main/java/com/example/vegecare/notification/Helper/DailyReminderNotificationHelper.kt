package com.example.vegecare.notification.Helper

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.vegecare.MainActivity
import com.example.vegecare.R
import com.example.vegecare.notification.BroadcastReceiver.DailyReminderReceiver
import com.example.vegecare.notification.Helper.WeatherNotificationHelper.getNotifications
import com.example.vegecare.notification.Helper.WeatherNotificationHelper.saveNotifications
import com.example.vegecare.notification.NotificationItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*

object DailyReminderNotificationHelper {

    private const val CHANNEL_ID = "daily_reminder_channel"
    private const val CHANNEL_NAME = "Daily Reminders"
    private const val CHANNEL_DESCRIPTION = "Notifications for daily plant care reminders"
    private const val REQUEST_CODE = 100
    private const val PREFS_NAME = "weather_notifications"
    private const val NOTIFICATIONS_KEY = "notifications"

    /**
     * Jadwalkan alarm untuk mengirimkan notifikasi pada jam tertentu setiap hari.
     */
    fun scheduleDailyReminder(context: Context) {
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, DailyReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 19)
            set(Calendar.MINUTE, 27)
            set(Calendar.SECOND, 0)
        }

        // Set alarm harian
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    /**
     * Buat notifikasi harian.
     */
    fun createDailyReminderNotification(context: Context, title: String, message: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = CHANNEL_DESCRIPTION
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Dapatkan daftar notifikasi yang sudah ada
        val currentNotifications = getNotifications(context)

        // Cek apakah sudah ada notifikasi yang dikirim hari ini
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val existingNotification = currentNotifications.find {
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it.timestamp)) == today
        }

        // Jika sudah ada notifikasi hari ini, jangan kirim lagi
        if (existingNotification == null) {

            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_water_drop_24)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .build()

            val notificationId = System.currentTimeMillis().toInt()
            notificationManager.notify(notificationId, notification)

            // Simpan notifikasi baru
            val newNotification = NotificationItem(notificationId, title, message, System.currentTimeMillis())
            saveNotifications(context, currentNotifications + newNotification)
        }
    }


    fun saveNotifications(context: Context, notifications: List<NotificationItem>) {
        val currentNotifications = getNotifications(context).toMutableList()
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
