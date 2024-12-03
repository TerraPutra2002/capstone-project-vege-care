package com.example.vegecare.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vegecare.R
import com.example.vegecare.databinding.ItemWeatherBinding
import com.example.vegecare.ui.home.data.response.CuacaItemItem

class WeatherAdapter(
    private val weatherData: List<CuacaItemItem?>, // Hanya menerima data cuaca
    private val lokasi: String // Lokasi hanya dikirimkan sekali
) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    class WeatherViewHolder(private val binding: ItemWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(forecast: CuacaItemItem?, lokasi: String) {
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
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding = ItemWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        // Pastikan data cuaca diteruskan ke adapter
        weatherData.getOrNull(position)?.let { holder.bind(it, lokasi) }
    }

    override fun getItemCount(): Int = weatherData.size
}
