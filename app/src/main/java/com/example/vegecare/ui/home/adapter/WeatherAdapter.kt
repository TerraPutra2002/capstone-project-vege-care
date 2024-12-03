package com.example.vegecare.ui.home.adapter

import android.os.Handler
import android.os.Looper
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vegecare.R
import com.example.vegecare.databinding.ItemWeatherBinding
import com.example.vegecare.ui.home.data.response.CuacaItemItem
import java.text.SimpleDateFormat
import java.util.*

class WeatherAdapter(
    private val weatherData: List<CuacaItemItem?>, // Hanya menerima data cuaca
    private val lokasi: String // Lokasi hanya dikirimkan sekali
) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    private val handler = Handler(Looper.getMainLooper())
    private var isFirstItem = false

    class WeatherViewHolder(private val binding: ItemWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(forecast: CuacaItemItem?, lokasi: String, isFirstItem: Boolean) {
            // Menampilkan lokasi sekali saja pada bagian header atau di bagian tertentu di layout
            binding.textViewLocation.text = "Lokasi: $lokasi"

            // Menampilkan data cuaca untuk setiap item
            binding.textViewForecastTemp.text = "Suhu: ${forecast?.t ?: "--"}°C"
            binding.textViewForecastDesc.text = "Cuaca: ${forecast?.weatherDesc ?: "--"}"
            binding.textViewForecastTime.text = "Jam: ${forecast?.localDatetime ?: "--"}"

            // Memuat gambar cuaca menggunakan Glide
            Glide.with(itemView.context)
                .load(forecast?.image)
                .placeholder(R.drawable.weather_image)
                .into(binding.imageViewForecast)

            // Menampilkan waktu saat ini hanya untuk item pertama
            if (isFirstItem) {
                val currentTime = Calendar.getInstance().time
                val formattedTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(currentTime)
                binding.textViewForecastTime.text = "Waktu saat ini: $formattedTime"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding = ItemWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        // Menandakan item pertama
        isFirstItem = position == 0
        weatherData.getOrNull(position)?.let { holder.bind(it, lokasi, isFirstItem) }

        // Jika item pertama, mulai memperbarui waktu setiap detik
        if (isFirstItem) {
            handler.postDelayed(object : Runnable {
                override fun run() {
                    notifyDataSetChanged() // Memperbarui RecyclerView untuk menampilkan waktu saat ini
                    handler.postDelayed(this, 1000) // Memanggil run() setiap detik (1000 ms)
                }
            }, 60000)
        }
    }

    override fun getItemCount(): Int = weatherData.size
}