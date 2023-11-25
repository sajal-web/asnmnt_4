package com.example.assignment_4.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment_4.databinding.ItemLocationBinding
import com.example.assignment_4.room.LocationEntity
import java.text.SimpleDateFormat
import java.util.*

class LocationAdapter : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    private var locations = emptyList<LocationEntity>()

    inner class LocationViewHolder(private val binding: ItemLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(location: LocationEntity) {
            binding.location =location
            binding.executePendingBindings()
            binding.latitudeTextView.text = "Latitude: ${location.latitude}"
            binding.longitudeTextView.text = "Longitude: ${location.longitude}"
            // Format timestamp
            val formattedTime = formatDate(location.timestamp)
            binding.locationTime.text = "Time & Date: $formattedTime"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLocationBinding.inflate(inflater, parent, false)
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val currentLocation = locations[position]
        holder.bind(currentLocation)
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    fun setLocations(locations: List<LocationEntity>) {
        this.locations = locations
        notifyDataSetChanged()
    }

    // Helper function to format timestamp
    private fun formatDate(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val date = Date(timestamp)
        return dateFormat.format(date)
    }
}
