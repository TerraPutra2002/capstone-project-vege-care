package com.example.vegecare.ui.notifications

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vegecare.databinding.FragmentNotificationsBinding
import com.example.vegecare.notification.Helper.DailyReminderNotificationHelper
import com.example.vegecare.notification.Helper.TemperatureNotificationHelper
import com.example.vegecare.notification.Helper.WeatherNotificationHelper
import com.example.vegecare.notification.NotificationAdapter
import com.example.vegecare.notification.NotificationItem
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class NotificationsFragment : Fragment() {

    private lateinit var binding: FragmentNotificationsBinding
    private lateinit var adapter: NotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun showDeleteConfirmation(notification: NotificationItem) {
        AlertDialog.Builder(requireContext())
            .setTitle("Selesaikan Aksi")
            .setMessage("Hebat! Apakah Anda sudah melakukan perawatan sesuai pengingat ini? Jika sudah, mari kita tandai sebagai selesai :D")
            .setPositiveButton("Ya") { _, _ ->
                adapter.removeItem(requireContext(), notification)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val weatherNotifications = WeatherNotificationHelper.getNotifications(requireContext())
        val temperatureNotifications =
            TemperatureNotificationHelper.getNotifications(requireContext())
        val dailyNotifications = DailyReminderNotificationHelper.getNotifications(requireContext())

        val currentDate = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val todayDate = dateFormat.format(currentDate.time)

        val allNotifications =
            (weatherNotifications + temperatureNotifications + dailyNotifications).filter { notification ->
                // Format timestamp notifikasi dan bandingkan dengan hari ini
                val notificationDate = dateFormat.format(Date(notification.timestamp))
                notificationDate == todayDate
            }.distinctBy { it.id }.distinctBy { it.title }
                .sortedByDescending { it.timestamp }

        if (allNotifications.isEmpty()) {
            binding.nullNotif.visibility = View.VISIBLE
        } else {
            binding.nullNotif.visibility = View.GONE
        }

        adapter = NotificationAdapter(allNotifications.toMutableList()) { notification ->
            showDeleteConfirmation(notification)
        }

        binding.recyclerViewNotifications.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewNotifications.adapter = adapter
    }
}
