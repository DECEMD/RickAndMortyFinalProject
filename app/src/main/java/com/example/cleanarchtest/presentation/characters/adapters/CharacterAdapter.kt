package com.example.cleanarchtest.presentation.characters.adapters

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cleanarchtest.databinding.ItemEpisodeListBinding
import com.example.cleanarchtest.domain.entities.Episode
import com.example.cleanarchtest.presentation.episodes.utils.EpisodesDiffCallback

class CharacterAdapter: RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    private lateinit var episodes: List<Episode>
    var onClick: (Episode) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding = ItemEpisodeListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(episodes[position])
        avoidMultipleClicks(holder.binding.root)
        holder.binding.root.setOnClickListener {
            onClick(episodes[position])
        }
    }

    override fun getItemCount(): Int {
        return episodes.size
    }

    private fun avoidMultipleClicks(view: View) {
        view.isClickable = false
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            view.isClickable = true
        }, 1000)
    }

    fun updateAdapter(updatedList: List<Episode>) {
        val result = DiffUtil.calculateDiff(EpisodesDiffCallback(episodes, updatedList))
        this.episodes = updatedList.toMutableList()
        result.dispatchUpdatesTo(this)
    }

    fun setData(episodes: List<Episode>) {
        this.episodes = episodes
    }

    class CharacterViewHolder(val binding: ItemEpisodeListBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(episode: Episode){
            binding.episodeNumber.text = episode.episode
            binding.episodeTitle.text = episode.name
            binding.episodeReleaseDate.text = episode.date
        }
    }
}
