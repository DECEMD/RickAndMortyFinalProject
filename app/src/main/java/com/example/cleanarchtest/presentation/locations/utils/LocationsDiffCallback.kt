package com.example.cleanarchtest.presentation.locations.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.cleanarchtest.domain.entities.Location

class LocationsDiffCallback(
    private val oldList: List<Location>,
    private val newList: List<Location>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}