package com.example.vegecare.ui.home.data.retrofit

import com.example.vegecare.ui.home.data.response.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServiceWeather {
    @GET("publik/prakiraan-cuaca")
    suspend fun getWeatherForecast(
        @Query("adm4") kodeWilayah: String
    ): Response<WeatherResponse>
}
