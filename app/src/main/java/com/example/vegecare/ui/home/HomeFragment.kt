package com.example.vegecare.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.vegecare.R
import com.example.vegecare.databinding.FragmentHomeBinding
import com.example.vegecare.ui.home.adapter.WeatherAdapter
import com.example.vegecare.ui.home.data.response.CuacaItemItem
import com.example.vegecare.ui.home.data.retrofit.ApiConfigWeather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import com.example.vegecare.ui.home.addplant.AddPlantActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var lokasi: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchWeatherData("35.78.22.1004")
    }

    private fun fetchWeatherData(kodeWilayah: String) {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiConfigWeather.weatherApi.getWeatherForecast(kodeWilayah)
                }

                if (response.isSuccessful) {
                    val weatherItems = response.body()?.data?.get(0)?.cuaca?.flatMap { it.orEmpty() }
                    Log.d("WeatherData", "Weather items: $weatherItems")
                    if (weatherItems.isNullOrEmpty()) {
                        showError("Data cuaca tidak tersedia")
                    } else {
                        val lokasiResponse = response.body()?.lokasi
                        lokasi = "${lokasiResponse?.desa}, ${lokasiResponse?.kecamatan}, ${lokasiResponse?.kota}"

                        setupViewPager(weatherItems)
                    }
                } else {
                    showError("Gagal memuat data: ${response.message()}")
                }
            } catch (e: Exception) {
                showError("Terjadi kesalahan: ${e.message}")
            }
        }
    }

    private fun setupViewPager(weatherData: List<CuacaItemItem?>) {
        val filteredData = weatherData.filterNotNull()

        val adapter = WeatherAdapter(filteredData, lokasi)
        binding.vpWeather.adapter = adapter

        binding.fabAddPlant.setOnClickListener {
            val intent = Intent(requireContext(), AddPlantActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
