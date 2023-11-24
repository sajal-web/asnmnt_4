package com.example.asnmnt_4.room

import android.app.Application
import android.os.AsyncTask

class LocationRepository(application: Application) {
    private val locationDao: LocationDao

    init {
        val db = LocationDatabase.getDatabase(application)
        locationDao = db.locationDao()
    }

    fun insert(location: LocationEntity) {
        InsertAsyncTask(locationDao).execute(location)
    }

    fun getLocationsBetweenTimes(startTime: Long, endTime: Long): List<LocationEntity> {
        return locationDao.getLocationsBetweenTimes(startTime, endTime)
    }

    private class InsertAsyncTask(private val asyncTaskDao: LocationDao) :
        AsyncTask<LocationEntity, Void, Void>() {
        override fun doInBackground(vararg params: LocationEntity): Void? {
            asyncTaskDao.insert(params[0])
            return null
        }
    }
}
