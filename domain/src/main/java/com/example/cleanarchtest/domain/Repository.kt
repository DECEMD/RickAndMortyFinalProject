package com.example.cleanarchtest.domain

import com.example.cleanarchtest.domain.entities.Character
import com.example.cleanarchtest.domain.entities.Characters
import com.example.cleanarchtest.domain.entities.CharactersFilter
import com.example.cleanarchtest.domain.entities.Episode
import com.example.cleanarchtest.domain.entities.Episodes
import com.example.cleanarchtest.domain.entities.EpisodesFilter
import com.example.cleanarchtest.domain.entities.Location
import com.example.cleanarchtest.domain.entities.Locations
import com.example.cleanarchtest.domain.entities.LocationsFilter

interface Repository {

    suspend fun getAllCharacters(page: Int, filter: CharactersFilter): Characters

    suspend fun getCharacterById(id: Int): Character

    suspend fun getCharactersById(id: String): List<Character>


    suspend fun getAllLocations(page: Int, filter: LocationsFilter): Locations

    suspend fun getLocationById(id: Int): Location

    suspend fun getLocationsById(id: String): List<Location>


    suspend fun getAllEpisodes(page: Int, filter: EpisodesFilter): Episodes

    suspend fun getEpisodeById(id: Int): Episode

    suspend fun getEpisodesById(id: String): List<Episode>

}