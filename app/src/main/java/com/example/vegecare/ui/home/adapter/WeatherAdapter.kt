package com.example.vegecare.ui.home.adapter

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vegecare.R
import com.example.vegecare.databinding.ItemWeatherBinding
import com.example.vegecare.ui.home.data.response.CuacaItemItem
import java.text.SimpleDateFormat
import java.util.*

class WeatherAdapter(
    private val weatherData: List<CuacaItemItem?>,
    private val lokasi: String
) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    private val handler = Handler(Looper.getMainLooper())
    private var isFirstItem = false

    private val limitedWeatherData = weatherData.take(7)

    class WeatherViewHolder(private val binding: ItemWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(forecast: CuacaItemItem?, lokasi: String, isFirstItem: Boolean) {
            binding.textViewLocation.text = "Lokasi: $lokasi"
            binding.textViewForecastTemp.text = "Suhu: ${forecast?.t ?: "--"}°C"
            binding.textViewForecastDesc.text = "Cuaca: ${forecast?.weatherDesc ?: "--"}"
            binding.textViewForecastTime.text = "Jam: ${forecast?.localDatetime ?: "--"}"

            val weatherImageResId = when {
                forecast?.weatherDesc?.contains("cerah berawan", ignoreCase = true) == true -> R.drawable.cerah_berawan
                forecast?.weatherDesc?.contains("hujan ringan", ignoreCase = true) == true -> R.drawable.hujan_ringan
                forecast?.weatherDesc?.contains("berawan", ignoreCase = true) == true -> R.drawable.berawan
                forecast?.weatherDesc?.contains("cerah", ignoreCase = true) == true -> R.drawable.cerah
                forecast?.weatherDesc?.contains("hujan lebat", ignoreCase = true) == true -> R.drawable.hujan_lebat
                forecast?.weatherDesc?.contains("petir", ignoreCase = true) == true -> R.drawable.badai_petir
                forecast?.weatherDesc?.contains("hujan", ignoreCase = true) == true -> R.drawable.hujan
                else -> R.drawable.weather_image
            }

            binding.imageViewForecast.setImageResource(weatherImageResId)

            if (isFirstItem) {
                val currentTime = Calendar.getInstance().time
                val formattedTime =
                    SimpleDateFormat("HH:mm", Locale.getDefault()).format(currentTime)
                binding.textViewForecastTime.text = "Waktu saat ini: $formattedTime"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding = ItemWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        isFirstItem = position == 0
        limitedWeatherData.getOrNull(position)?.let { holder.bind(it, lokasi, isFirstItem) }

        if (isFirstItem) {
            handler.postDelayed(object : Runnable {
                override fun run() {
                    notifyDataSetChanged()
                    handler.postDelayed(this, 1000)
                }
            }, 60000)
        }
    }

    override fun getItemCount(): Int = limitedWeatherData.size
}
