package com.example.vegecare.ui.detect.data.response

import com.google.gson.annotations.SerializedName

data class PlantDeseaseResponse(

	@field:SerializedName("prediction")
	val prediction: Prediction? = null,

	@field:SerializedName("plant_care")
	val plantCare: PlantCare? = null
)

data class PlantCare(

	@field:SerializedName("general_care")
	val generalCare: String? = null,

	@field:SerializedName("treatment")
	val treatment: String? = null,

	@field:SerializedName("general_info")
	val generalInfo: String? = null,

	@field:SerializedName("prevention")
	val prevention: String? = null
)

data class Prediction(

	@field:SerializedName("condition")
	val condition: String? = null,

	@field:SerializedName("confidence")
	val confidence: Any? = null,

	@field:SerializedName("plant_name")
	val plantName: String? = null
)
