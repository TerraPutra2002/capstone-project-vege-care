package com.example.vegecare.data.plant.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PlantDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(plant: Plant)

    @Update
    suspend fun update(plant: Plant)

    @Delete
    suspend fun delete(plant: Plant)

    @Query("SELECT * FROM plant_table")
    fun getAllPlants(): LiveData<List<Plant>>
}