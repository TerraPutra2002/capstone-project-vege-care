package com.example.vegecare.ui.home

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
import java.text.SimpleDateFormat
import java.util.*

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
        binding.ProgressBar.visibility = View.VISIBLE
        fetchWeatherData("35.78.22.1004")

    }

    private fun fetchWeatherData(kodeWilayah: String) {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiConfigWeather.weatherApi.getWeatherForecast(kodeWilayah)
                }

                binding.ProgressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val weatherItems = response.body()?.data?.get(0)?.cuaca?.flatMap { it.orEmpty() }
                    Log.d("WeatherData", "Weather items: $weatherItems")
                    if (weatherItems.isNullOrEmpty()) {
                        showError("Data cuaca tidak tersedia")
                    } else {
                        val lokasiResponse = response.body()?.lokasi
                        lokasi = "${lokasiResponse?.desa}, ${lokasiResponse?.kecamatan}, ${lokasiResponse?.kota}"

                        // Filter data cuaca berdasarkan waktu
                        val filteredWeatherItems = filterWeatherData(weatherItems)

                        setupViewPager(filteredWeatherItems)
                    }
                } else {
                    showError("Gagal memuat data: ${response.message()}")
                }
            } catch (e: Exception) {
                binding.ProgressBar.visibility = View.GONE
                showError("Terjadi kesalahan: ${e.message}")
            }
        }
    }

    private fun filterWeatherData(weatherItems: List<CuacaItemItem?>): List<CuacaItemItem?> {
        val currentDate = Calendar.getInstance() // Waktu saat ini

        // Menggunakan SimpleDateFormat untuk mengkonversi string waktu menjadi objek Date
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()) // Sesuaikan dengan format waktu yang digunakan

        // Memfilter data cuaca berdasarkan waktu (menghapus data lebih dari 3 jam yang lalu)
        return weatherItems.filterNotNull().filter { forecast ->
            try {
                // Parsing waktu dari data cuaca
                val forecastTime = dateFormat.parse(forecast.localDatetime)

                // Cek apakah waktu cuaca lebih dari 3 jam yang lalu
                val timeDifference = currentDate.timeInMillis - (forecastTime?.time ?: 0)
                val threeHoursInMillis = 3 * 60 * 60 * 1000 // 3 jam dalam milidetik

                // Hanya ambil data yang waktu cuacanya tidak lebih dari 3 jam yang lalu
                timeDifference <= threeHoursInMillis
            } catch (e: Exception) {
                false // Jika terjadi kesalahan parsing waktu, abaikan data ini
            }
        }
    }

    private fun setupViewPager(weatherData: List<CuacaItemItem?>) {
        val filteredData = weatherData.filterNotNull()

        val adapter = WeatherAdapter(filteredData, lokasi)
        binding.vpWeather.adapter = adapter
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
