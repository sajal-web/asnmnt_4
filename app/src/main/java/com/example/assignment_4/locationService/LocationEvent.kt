package com.example.assignment_4.locationService

data class LocationEvent(
    val latitude:Double?,
    val longitude:Double?,
    val timestamp: Long = System.currentTimeMillis()
)
