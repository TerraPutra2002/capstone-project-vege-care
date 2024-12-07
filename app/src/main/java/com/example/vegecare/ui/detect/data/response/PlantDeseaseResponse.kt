package com.example.vegecare.ui.detect.data.response

import com.google.gson.annotations.SerializedName

data class PlantDeseaseResponse(

	@field:SerializedName("disease_prediction")
	val diseasePrediction: DiseasePrediction? = null,

	@field:SerializedName("plant_care")
	val plantCare: PlantCare? = null
)

data class DiseasePrediction(

	@field:SerializedName("confidence")
	val confidence: Any? = null,

	@field:SerializedName("class")
	val jsonMemberClass: String? = null
)

data class PlantCare(

	@field:SerializedName("general_care")
	val generalCare: String? = null,

	@field:SerializedName("treatment")
	val treatment: String? = null,

	@field:SerializedName("prevention")
	val prevention: String? = null
)
