package com.example.cleanarchtest.presentation.episodes.adapters

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

class EpisodesAdapter : RecyclerView.Adapter<EpisodesAdapter.EpisodesViewHolder>() {

    private lateinit var episodes: List<Episode>
    var onClick: (Episode) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodesViewHolder {
        val binding = ItemEpisodeListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EpisodesViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return episodes.size
    }

    override fun onBindViewHolder(holder: EpisodesViewHolder, position: Int) {
        holder.bind(episodes[position])
        avoidMultipleClicks(holder.binding.root)
        holder.binding.root.setOnClickListener {
            onClick(episodes[position])
        }
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

    inner class EpisodesViewHolder(val binding: ItemEpisodeListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(episode: Episode) = with(binding){
            episodeTitle.text = episode.name
            episodeNumber.text = episode.episode
            episodeReleaseDate.text = episode.date
        }
    }
}