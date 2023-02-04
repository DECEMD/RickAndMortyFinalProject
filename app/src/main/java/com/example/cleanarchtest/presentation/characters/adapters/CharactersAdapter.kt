package com.example.cleanarchtest.presentation.characters.adapters

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cleanarchtest.databinding.ItemCharacterListBinding
import com.example.cleanarchtest.domain.entities.Character
import com.example.cleanarchtest.presentation.characters.utils.CharactersDiffCallback

class CharactersAdapter : RecyclerView.Adapter<CharactersAdapter.CharactersViewHolder>() {

    private lateinit var characters: List<Character>
    var onClick: (Character) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        val binding = ItemCharacterListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CharactersViewHolder(binding)
    }

    override fun getItemCount(): Int = characters.size

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        holder.bind(characters[position])
        avoidMultipleClicks(holder.binding.root)
        holder.binding.root.setOnClickListener {
            onClick(characters[position])
        }
    }

    private fun avoidMultipleClicks(view: View) {
        view.isClickable = false
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            view.isClickable = true
        }, 1000)
    }

    fun updateAdapter(updatedList: List<Character>) {
        val result = DiffUtil.calculateDiff(CharactersDiffCallback(characters, updatedList))
        this.characters = updatedList.toMutableList()
        result.dispatchUpdatesTo(this)
    }

    fun setData(characters: List<Character>) {
        this.characters = characters
    }

    class CharactersViewHolder(val binding: ItemCharacterListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(character: Character) = with(binding){
            characterTitle.text = character.name
            characterSpecies.text = character.species
            characterStatus.text = character.status
            characterGender.text = character.gender
            Glide
                .with(this.root)
                .load(character.image)
                .into(characterImage)
        }
    }

}