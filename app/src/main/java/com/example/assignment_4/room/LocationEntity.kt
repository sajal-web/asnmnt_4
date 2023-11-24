package com.example.asnmnt_4.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_table")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var latitude: Double,
    var longitude: Double,
    var timestamp: Long // To store the time of registration
)

