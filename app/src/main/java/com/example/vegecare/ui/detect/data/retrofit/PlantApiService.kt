package com.example.vegecare.ui.detect.data.retrofit

import com.example.vegecare.ui.detect.data.response.PlantDeseaseResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PlantApiService {
    @Multipart
    @POST("predict")
    fun getPlantDiseasePrediction(
        @Part image: MultipartBody.Part
    ): Call<PlantDeseaseResponse>
}