package com.example.vegecare.ui.home.addplant

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.vegecare.data.plant.database.Plant
import com.example.vegecare.data.plant.repository.PlantRepository
import kotlinx.coroutines.launch

class AddPlantViewModel(private val repository: PlantRepository) : ViewModel() {
    val allPlants: LiveData<List<Plant>> = repository.getAllPlants()


    fun insertPlant(plant: Plant) {
        viewModelScope.launch {
            repository.insertPlant(plant)
        }
    }

    fun getPlantById(plantId: Int): LiveData<Plant?> = liveData {
        emit(repository.getPlantById(plantId))
    }

    fun updateJumlahHidup(plantId: Int, hidup: Int) {
        viewModelScope.launch {
            repository.updateJumlahHidup(plantId, hidup)
        }
    }
}