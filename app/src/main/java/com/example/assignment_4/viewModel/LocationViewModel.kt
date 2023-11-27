package com.example.assignment_4.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.assignment_4.room.LocationDatabase
import com.example.assignment_4.room.LocationEntity
import com.example.assignment_4.room.LocationRepository
import kotlinx.coroutines.launch

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LocationRepository
    val allLocations: LiveData<List<LocationEntity>>

    init {
        val locationDao = LocationDatabase.getDatabase(application).locationDao()
        repository = LocationRepository(locationDao)
        allLocations = repository.allLocations
    }
    fun updateLocation(locationEntity: LocationEntity) {
        viewModelScope.launch {
            repository.insertLocation(locationEntity)
        }
    }

//    fun insertLocation(location: LocationEntity) {
//        viewModelScope.launch {
//            repository.insertLocation(location)
//        }
//    }
    fun getLocationsInDateRange(fromDate: Long, toDate: Long): LiveData<List<LocationEntity>> {
        return repository.getLocationsInDateRange(fromDate, toDate)
    }
}
