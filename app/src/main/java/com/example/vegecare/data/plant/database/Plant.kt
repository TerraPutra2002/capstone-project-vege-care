package com.example.vegecare.data.plant.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plant_table")
data class Plant(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nama: String,
    val jenis: String,
    val media: String,
    val jumlah: Int,
    val hidup: Int
)