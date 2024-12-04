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
import com.example.vegecare.notification.Helper.DailyReminderNotificationHelper
import com.example.vegecare.notification.Helper.WeatherNotificationHelper
import com.example.vegecare.notification.Helper.TemperatureNotificationHelper
import com.example.vegecare.ui.home.adapter.WeatherAdapter
import com.example.vegecare.ui.home.data.response.CuacaItemItem
import com.example.vegecare.ui.home.data.retrofit.ApiConfigWeather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

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
        binding.ProgressBar.visibility = View.VISIBLE
        DailyReminderNotificationHelper.scheduleDailyReminder(requireContext())
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
                    val weatherItems =
                        response.body()?.data?.get(0)?.cuaca?.flatMap { it.orEmpty() }
                    Log.d("WeatherData", "Weather items: $weatherItems")
                    if (weatherItems.isNullOrEmpty()) {
                        showError("Data cuaca tidak tersedia")
                    } else {
                        val lokasiResponse = response.body()?.lokasi
                        lokasi =
                            "${lokasiResponse?.desa}, ${lokasiResponse?.kecamatan}, ${lokasiResponse?.kota}"

                        // Tampilkan notifikasi untuk Hujan Petir
                        val hujanPetirItems =
                            weatherItems.filter { it?.weatherDesc == "Hujan Petir" }
                        hujanPetirItems.forEach { forecast ->
                            val waktu = forecast?.localDatetime ?: "Waktu tidak diketahui"
                            val cuaca = forecast?.weatherDesc ?: "Cuaca belum terprediksi"
                            WeatherNotificationHelper.createNotification(
                                requireContext(),
                                "Peringatan Cuaca Eksrem",
                                "$cuaca diprediksi pada $waktu, segera berikan tindakan pada tanaman Anda"
                            )
                        }

                        // Tampilkan notifikasi untuk Suhu Tinggi
                        val suhuTinggiItems = weatherItems.filter {
                            (it?.t ?: 0) >= 38 // Jika t null, gunakan default 0
                        }

                        suhuTinggiItems.forEach { forecast ->
                            val waktu = forecast?.localDatetime ?: "Waktu tidak diketahui"
                            val suhu = forecast?.t ?: "Suhu belum terprediksi"
                            TemperatureNotificationHelper.createTemperatureNotification(
                                requireContext(),
                                "Peringatan Suhu Tinggi",
                                "Diperkirakan suhu mencapai $suhu°C pada $waktu, segera berikan tindakan pada tanaman Anda"
                            )
                        }

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
        val currentDate = Calendar.getInstance()

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        return weatherItems.filterNotNull().filter { forecast ->
            try {
                val forecastTime = dateFormat.parse(forecast.localDatetime)

                val timeDifference = currentDate.timeInMillis - (forecastTime?.time ?: 0)
                val threeHoursInMillis = 3 * 60 * 60 * 1000

                timeDifference <= threeHoursInMillis
            } catch (e: Exception) {
                false
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
