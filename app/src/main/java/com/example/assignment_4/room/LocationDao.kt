package com.example.asnmnt_4.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LocationDao {
    @Insert
    fun insert(location: LocationEntity)

    @Query("SELECT * FROM location_table WHERE timestamp BETWEEN :startTime AND :endTime")
    fun getLocationsBetweenTimes(startTime: Long, endTime: Long): List<LocationEntity>
}
