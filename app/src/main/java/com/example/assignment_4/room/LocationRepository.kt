package com.example.assignment_4.room

import androidx.lifecycle.LiveData

class LocationRepository(private val locationDao: LocationDao) {

    val allLocations: LiveData<List<LocationEntity>> = locationDao.getAllLocations()

    suspend fun insertLocation(location: LocationEntity) {
        locationDao.insertLocation(location)
    }
}