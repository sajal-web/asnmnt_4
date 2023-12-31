package com.example.assignment_4.room
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LocationDao {
    @Insert
    suspend fun insertLocation(location: LocationEntity)

    @Query("SELECT * FROM location_table ORDER BY timestamp DESC")
    fun getAllLocations(): LiveData<List<LocationEntity>>

    @Query("SELECT * FROM location_table WHERE timestamp BETWEEN :fromDate AND :toDate ORDER BY timestamp DESC")
    fun getLocationsInDateRange(fromDate: Long, toDate: Long): LiveData<List<LocationEntity>>


}
