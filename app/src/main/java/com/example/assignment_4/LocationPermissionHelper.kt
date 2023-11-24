package com.example.assignment_4

import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class LocationPermissionHelper(
    private val activity: AppCompatActivity,
    private val onPermissionGranted: () -> Unit
) {

    private val backgroundLocation =
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                onPermissionGranted()
            }
        }

    private val locationPermissions =
        activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            when {
                it.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        if (ActivityCompat.checkSelfPermission(
                                activity,
                                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            backgroundLocation.launch(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                        }
                    }

                }
                it.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    onPermissionGranted()
                }
            }
        }

    fun checkPermissions() {
        if (ActivityCompat.checkSelfPermission(
                activity,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                activity,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissions.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            onPermissionGranted()
        }
    }
}
