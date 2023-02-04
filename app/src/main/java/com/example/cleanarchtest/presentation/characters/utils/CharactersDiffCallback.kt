package com.example.cleanarchtest.presentation.characters.utils

import com.example.cleanarchtest.domain.entities.Character
import androidx.recyclerview.widget.DiffUtil

class CharactersDiffCallback(
    private val oldList: List<Character>,
    private val newList: List<Character>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}