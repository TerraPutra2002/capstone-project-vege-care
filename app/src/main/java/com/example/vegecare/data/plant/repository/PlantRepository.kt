package com.example.vegecare.data.plant.repository

import android.content.Context
import com.example.vegecare.data.plant.database.Plant
import com.example.vegecare.data.plant.database.PlantDao
import com.example.vegecare.data.plant.database.PlantDatabase

class PlantRepository (private val plantDao: PlantDao) {

    suspend fun insertPlant(plant: Plant) {
        plantDao.insert(plant)
    }

    fun getAllPlants() = plantDao.getAllPlants()

    suspend fun getPlantById(plantId: Int): Plant? {
        return plantDao.getPlantById(plantId)
    }

    suspend fun updateJumlahHidup(plantId: Int, newJumlahHidup: Int) {
        plantDao.updateJumlahHidup(plantId, newJumlahHidup)
    }

    companion object {
        @Volatile
        private var instance: PlantRepository? = null

        fun getInstance(plantDao: PlantDao): PlantRepository {
            return instance ?: synchronized(this) {
                instance ?: PlantRepository(plantDao).also { instance = it }
            }
        }
    }
}