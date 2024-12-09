package com.example.vegecare.data.plant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vegecare.data.plant.repository.PlantRepository
import com.example.vegecare.ui.home.addplant.AddPlantViewModel

class PlantViewModelFactory(private val plantRepository: PlantRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddPlantViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddPlantViewModel(plantRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}