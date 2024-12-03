package com.example.vegecare.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vegecare.databinding.ItemWeatherBinding

class WeatherAdapter(private val weatherData: List<WeatherItem>) :
    RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

        class WeatherViewHolder(private val binding: ItemWeatherBinding) :
                RecyclerView.ViewHolder(binding.root) {

                    fun bind(item: WeatherItem) {
                        binding.tvTitleWeather.text = item.title
                        binding.tvDescriptionWeather.text = item.description
                    }
                }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding = ItemWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(weatherData[position])
    }

    override fun getItemCount(): Int = weatherData.size
}

data class WeatherItem(val title: String, val description: String)