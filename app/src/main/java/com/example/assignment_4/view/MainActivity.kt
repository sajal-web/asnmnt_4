package com.example.assignment_4.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment_4.LocationEvent
import com.example.assignment_4.LocationPermissionHelper
import com.example.assignment_4.LocationService
import com.example.assignment_4.adapter.LocationAdapter
import com.example.assignment_4.databinding.ActivityMainBinding
import com.example.assignment_4.viewModel.LocationViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MainActivity : AppCompatActivity() {
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var locationAdapter: LocationAdapter
    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding!!

    private var service: Intent? = null

    private val locationPermissionHelper = LocationPermissionHelper(this) {
        // Permission granted callback
        startService(service)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        locationAdapter = LocationAdapter(this)
        service = Intent(this, LocationService::class.java)

        // starting the service
        startService(service)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = locationAdapter
        binding.btnSearch.setOnClickListener {
            val intent = Intent(this, SearchResultDetails::class.java)
            startActivity(intent)
        }
        // Observe the LiveData in the ViewModel
        locationViewModel.allLocations.observe(this, Observer { locations ->
            // Update the adapter's data set
            locationAdapter.setLocations(locations)
        })



        binding.apply {
            btnStartLocationTracking.setOnClickListener {
                locationPermissionHelper.checkPermissions()
            }

            btnRemoveLocationTracking.setOnClickListener {
                stopService(service)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    @Subscribe
    fun receiveLocationEvent(locationEvent: LocationEvent) {
        binding.tvLatitude.text = "Latitude -> ${locationEvent.latitude}"
        binding.tvLongitude.text = "Longitude -> ${locationEvent.longitude}"
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        stopService(service)
//        if (EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().unregister(this)
//        }

}

