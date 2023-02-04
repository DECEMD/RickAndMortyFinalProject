package com.example.cleanarchtest.domain.Interactors

import com.example.cleanarchtest.domain.Repository
import com.example.cleanarchtest.domain.entities.Episode
import com.example.cleanarchtest.domain.entities.Episodes
import com.example.cleanarchtest.domain.entities.EpisodesFilter
import javax.inject.Inject

interface IEpisodeInteractor {

    suspend fun getAllEpisodes(page: Int, filter: EpisodesFilter): Episodes

    suspend fun getEpisodeById(id: Int): Episode

    suspend fun getEpisodesById(id: String): List<Episode>

}

class EpisodeInteractor @Inject constructor(private val repository: Repository) :
    IEpisodeInteractor {

    override suspend fun getAllEpisodes(page: Int, filter: EpisodesFilter) = repository.getAllEpisodes(page, filter)

    override suspend fun getEpisodeById(id: Int): Episode = repository.getEpisodeById(id)

    override suspend fun getEpisodesById(id: String): List<Episode> = repository.getEpisodesById(id)

}