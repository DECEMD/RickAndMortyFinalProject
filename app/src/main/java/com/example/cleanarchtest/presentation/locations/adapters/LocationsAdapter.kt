package com.example.cleanarchtest.presentation.locations.adapters

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cleanarchtest.databinding.ItemLocationsListBinding
import com.example.cleanarchtest.domain.entities.Location
import com.example.cleanarchtest.presentation.locations.utils.LocationsDiffCallback

class LocationsAdapter : RecyclerView.Adapter<LocationsAdapter.LocationsViewHolder>() {

    private lateinit var locations: List<Location>

    var onClick: (Location) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationsViewHolder {
        val binding = ItemLocationsListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LocationsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    override fun onBindViewHolder(holder: LocationsViewHolder, position: Int) {
        holder.bind(locations[position])
        avoidMultipleClicks(holder.binding.root)
        holder.binding.root.setOnClickListener {
            onClick(locations[position])
        }
    }

    private fun avoidMultipleClicks(view: View) {
        view.isClickable = false
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            view.isClickable = true
        }, 1000)
    }

    fun updateAdapter(updatedList: List<Location>) {
        val result = DiffUtil.calculateDiff(LocationsDiffCallback(locations, updatedList))
        this.locations = updatedList.toMutableList()
        result.dispatchUpdatesTo(this)
    }

    fun setData(locations: List<Location>) {
        this.locations = locations
    }

    inner class LocationsViewHolder(val binding: ItemLocationsListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(location: Location) = with(binding) {
            locationName.text = location.name
            locationDimension.text = location.dimension
            locationType.text = location.type
        }
    }
}