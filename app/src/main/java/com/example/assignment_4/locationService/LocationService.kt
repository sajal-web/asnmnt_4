package com.example.assignment_4.locationService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.example.assignment_4.room.LocationEntity
import com.example.assignment_4.viewModel.LocationViewModel
import com.google.android.gms.location.*
import org.greenrobot.eventbus.EventBus

class LocationService : Service() {

    private val locationViewModel: LocationViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory(application)
            .create(LocationViewModel::class.java)
    }
    companion object {
        const val CHANNEL_ID = "12345"
        const val NOTIFICATION_ID = 12345
    }

    private val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    private val locationRequest: LocationRequest by lazy {
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 20000)
            .setIntervalMillis(20000)
            .build()
    }

    private val locationCallback: LocationCallback by lazy {
        object : LocationCallback() {
            override fun onLocationAvailability(p0: LocationAvailability) {
                super.onLocationAvailability(p0)
            }

            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                onNewLocation(locationResult)
            }
        }
    }

    private val notificationManager: NotificationManagerCompat by lazy {
        NotificationManagerCompat.from(this)
    }

    private var location: Location? = null

    override fun onCreate() {
        super.onCreate()

        notificationManager.createNotificationChannel(
            NotificationChannel(CHANNEL_ID, "locations", NotificationManager.IMPORTANCE_HIGH)
        )
    }

    @Suppress("MissingPermission")
    private fun createLocationRequest() {
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback, null
        )
    }
    private fun onNewLocation(locationResult: LocationResult) {
        location = locationResult.lastLocation
        EventBus.getDefault().post(
            LocationEvent(
                latitude = location?.latitude,
                longitude = location?.longitude
            )
        )
        // Update ViewModel with the latest location
        locationViewModel.updateLocation(
            LocationEntity(
                latitude = location?.latitude,
                longitude = location?.longitude
            )
        )
        startForeground(NOTIFICATION_ID, getEmptyNotification())

    }
    private fun getEmptyNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Location Updates")
            .setContentText("Service is running in the background")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .setChannelId(CHANNEL_ID)
            .build()
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        createLocationRequest()
        return START_STICKY
    }
    override fun onBind(intent: Intent): IBinder? = null
}
