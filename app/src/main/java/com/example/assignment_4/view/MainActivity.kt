package com.example.assignment_4.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment_4.locationService.LocationEvent
import com.example.assignment_4.locationService.LocationPermissionHelper
import com.example.assignment_4.locationService.LocationService
import com.example.assignment_4.adapter.LocationAdapter
import com.example.assignment_4.databinding.ActivityMainBinding
import com.example.assignment_4.fragment.DateTimePickerFragment
import com.example.assignment_4.viewModel.LocationViewModel
import com.google.android.material.textfield.TextInputEditText
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity(), DateTimePickerFragment.OnDateTimeSetListener {

    // Declare variables
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var locationAdapter: LocationAdapter
    private var clickedEditText: TextInputEditText? = null
    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding!!

    private var service: Intent? = null
    private lateinit var fromDate: Date
    private lateinit var toDate: Date

    private val locationPermissionHelper = LocationPermissionHelper(
        this,
        { startService(service) },
        { Toast.makeText(this, "Please enable GPS to track location", Toast.LENGTH_SHORT).show() }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeViewModelAndAdapter()
        setupService()
        setupUI()
        setupClickListeners()
        observeLocationUpdates()
    }

    private fun initializeViewModelAndAdapter() {
        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        locationAdapter = LocationAdapter()
    }

    private fun setupService() {
        service = Intent(this, LocationService::class.java)
        startService(service)
    }

    private fun setupUI() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = locationAdapter
    }

    private fun setupClickListeners() {
        binding.apply {
            btnStartLocationTracking.setOnClickListener {
                locationPermissionHelper.checkGpsAndPermission()
            }

            btnRemoveLocationTracking.setOnClickListener {
                stopService(service)
            }

            editTextFrom.setOnClickListener {
                clickedEditText = editTextFrom
                showDatePickerDialog()
            }

            editTextTo.setOnClickListener {
                clickedEditText = editTextTo
                showDatePickerDialog()
            }

            btnSearch.setOnClickListener {
                handleSearchButtonClick()
            }
        }
    }

    private fun observeLocationUpdates() {
        locationViewModel.allLocations.observe(this, Observer { locations ->
            locationAdapter.setLocations(locations)
        })
    }

    private fun handleSearchButtonClick() {
        if (::fromDate.isInitialized && ::toDate.isInitialized) {
            if (fromDate <= toDate) {
                locationViewModel.getLocationsInDateRange(fromDate.time, toDate.time)
                    .observe(this, Observer { locations ->
                        locationAdapter.setLocations(locations)
                        Log.d("locationUpdateDate", locations.toString())
                    })
            } else {
                Toast.makeText(this, "Invalid date range", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please select From and To dates", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDatePickerDialog() {
        val dateTimePickerFragment = DateTimePickerFragment()
        dateTimePickerFragment.setOnDateTimeSetListener(this)
        dateTimePickerFragment.show(supportFragmentManager, "dateTimePicker")
    }

    override fun onDateTimeSet(dateTime: Date) {
        val formattedDateTime = dateTime.formatDateTime()
        runOnUiThread {
            when (clickedEditText) {
                binding.editTextFrom -> {
                    fromDate = dateTime
                    binding.editTextFrom.setText(formattedDateTime)
                }
                binding.editTextTo -> {
                    toDate = dateTime
                    binding.editTextTo.setText(formattedDateTime)
                }
            }
        }
    }

    private fun Date.formatDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return dateFormat.format(this)
    }

    override fun onStart() {
        super.onStart()
        registerEventBus()
    }

    @Subscribe
    fun receiveLocationEvent(locationEvent: LocationEvent) {
        binding.tvLatitude.text = "Latitude -> ${locationEvent.latitude}"
        binding.tvLongitude.text = "Longitude -> ${locationEvent.longitude}"
    }

    private fun registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        stopService(service)
//        unregisterEventBus()
//    }

//    private fun unregisterEventBus() {
//        if (EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().unregister(this)
//        }
//    }
}

