package com.example.vegecare.notification

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vegecare.databinding.ItemNotificationBinding
import com.example.vegecare.notification.Helper.DailyReminderNotificationHelper
import com.example.vegecare.notification.Helper.TemperatureNotificationHelper
import com.example.vegecare.notification.Helper.WeatherNotificationHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationAdapter(
    private val notifications: MutableList<NotificationItem>,
    private val onItemClick: (NotificationItem) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    class NotificationViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(notification: NotificationItem, onLongClick: (NotificationItem) -> Unit) {
            binding.textViewTitle.text = notification.title
            binding.textViewMessage.text = notification.message
            binding.textViewTimestamp.text = SimpleDateFormat(
                "dd MMM yyyy, HH:mm", Locale.getDefault()
            ).format(Date(notification.timestamp))

            binding.root.setOnClickListener {
                onLongClick(notification)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ItemNotificationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(notifications[position], onItemClick)
    }

    override fun getItemCount(): Int = notifications.size

    fun removeItem(context: Context, notification: NotificationItem) {
        val position = notifications.indexOf(notification)
        if (position != -1) {
            // Hapus dari daftar lokal
            notifications.removeAt(position)
            notifyItemRemoved(position)

            // Perbarui setiap kategori notifikasi
            val weatherNotifications = WeatherNotificationHelper.getNotifications(context).toMutableList()
            if (weatherNotifications.remove(notification)) {
                WeatherNotificationHelper.saveNotifications(context, weatherNotifications)
                return
            }

            val temperatureNotifications = TemperatureNotificationHelper.getNotifications(context).toMutableList()
            if (temperatureNotifications.remove(notification)) {
                TemperatureNotificationHelper.saveNotifications(context, temperatureNotifications)
                return
            }

            val dailyNotifications = DailyReminderNotificationHelper.getNotifications(context).toMutableList()
            if (dailyNotifications.remove(notification)) {
                DailyReminderNotificationHelper.saveNotifications(context, dailyNotifications)
                return
            }
        }
    }

    val currentNotifications: List<NotificationItem>
        get() = notifications
}
