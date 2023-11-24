package com.example.assignment_4.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment_4.R
import com.example.assignment_4.room.LocationEntity
import java.text.SimpleDateFormat
import java.util.*

class LocationAdapter(private val context: Context) :
    RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    private var locations = emptyList<LocationEntity>()

    inner class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val latitudeTextView: TextView = itemView.findViewById(R.id.textViewLatitude)
        val longitudeTextView: TextView = itemView.findViewById(R.id.textViewLongitude)
        val locationTime : TextView = itemView.findViewById(R.id.textViewTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_location, parent, false)
        return LocationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val currentLocation = locations[position]

        holder.latitudeTextView.text = "Latitude: ${currentLocation.latitude}"
        holder.longitudeTextView.text = "Longitude: ${currentLocation.longitude}"
        // Format timestamp
        val formattedTime = formatDate(currentLocation.timestamp)
        holder.locationTime.text = "Time & Date: $formattedTime"
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

