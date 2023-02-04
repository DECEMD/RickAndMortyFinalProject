package com.example.cleanarchtest.data

import com.example.cleanarchtest.data.network.Api
import com.example.cleanarchtest.data.storage.DatabaseStorage
import com.example.cleanarchtest.domain.Repository
import com.example.cleanarchtest.domain.entities.Characters
import com.example.cleanarchtest.domain.entities.Character
import com.example.cleanarchtest.domain.entities.CharactersFilter
import com.example.cleanarchtest.domain.entities.Episode
import com.example.cleanarchtest.domain.entities.Episodes
import com.example.cleanarchtest.domain.entities.EpisodesFilter
import com.example.cleanarchtest.domain.entities.Info
import com.example.cleanarchtest.domain.entities.LocationsFilter
import com.example.cleanarchtest.domain.entities.Location
import com.example.cleanarchtest.domain.entities.Locations
import com.example.cleanarchtest.domain.mappers.CharacterToEntityMapper
import com.example.cleanarchtest.domain.mappers.EntityToCharacterMapper
import com.example.cleanarchtest.domain.mappers.EntityToEpisodeMapper
import com.example.cleanarchtest.domain.mappers.EntityToLocationMapper
import com.example.cleanarchtest.domain.mappers.EpisodeToEntityMapper
import com.example.cleanarchtest.domain.mappers.LocationToEntityMapper
import javax.inject.Inject

class RepositoryImplementation @Inject constructor(
    private val databaseStorage: DatabaseStorage,
    private val rickAndMortyApi: Api
) : Repository {

    override suspend fun getAllCharacters(page: Int, filter: CharactersFilter) = try {
        val res = rickAndMortyApi.getAllCharacters(
            page,
            filter.name,
            filter.status,
            filter.gender,
            filter.species
        )
        databaseStorage.rickAndMortyDao.saveAllCharacters(CharacterToEntityMapper.map(res.results))
        res
    } catch (e: Exception) {
        val list = EntityToCharacterMapper.map(
            databaseStorage.rickAndMortyDao.getAllCharacters(
                if (filter.name.isNullOrEmpty()) filter.name else "%${filter.name}%",
                filter.status,
                filter.gender,
                filter.species
            )
        )
        Characters(list, Info(1))
    }

    override suspend fun getCharacterById(id: Int): Character = try {
        rickAndMortyApi.getCharacterById(id)
    } catch (e: Exception) {
        EntityToCharacterMapper.mapOne(databaseStorage.rickAndMortyDao.getCharacterById(id))
    }

    override suspend fun getCharactersById(id: String): List<Character> = try {
        rickAndMortyApi.getCharactersById(id)
    } catch (e: Exception) {
        EntityToCharacterMapper.map(databaseStorage.rickAndMortyDao.getCharactersById(id))
    }


    override suspend fun getAllLocations(page: Int, filter: LocationsFilter) = try {
        val res = rickAndMortyApi.getAllLocations(page, filter.name, filter.type)
        databaseStorage.rickAndMortyDao.saveAllLocations(LocationToEntityMapper.map(res.results))
        res
    } catch (e: Exception) {
        val list = EntityToLocationMapper.map(
            databaseStorage.rickAndMortyDao.getAllLocations(
                if (filter.name.isNullOrEmpty()) filter.name else "%${filter.name}%",
                filter.type
            )
        )
        Locations(list, Info(1))
    }

    override suspend fun getLocationById(id: Int): Location = try {
        rickAndMortyApi.getLocationById(id)
    } catch (e: Exception) {
        EntityToLocationMapper.mapOne(databaseStorage.rickAndMortyDao.getLocationById(id))
    }

    override suspend fun getLocationsById(id: String): List<Location> = try {
        rickAndMortyApi.getLocationsById(id)
    } catch (e: Exception) {
        EntityToLocationMapper.map(databaseStorage.rickAndMortyDao.getLocationsById(id))
    }


    override suspend fun getAllEpisodes(page: Int, filter: EpisodesFilter) = try {
        val res = rickAndMortyApi.getAllEpisodes(page, filter.name, filter.season)
        databaseStorage.rickAndMortyDao.saveAllEpisodes(
            EpisodeToEntityMapper.map(res.results)
        )
        res
    } catch (e: Exception) {
        val list = EntityToEpisodeMapper.map(databaseStorage.rickAndMortyDao.getAllEpisodes(
                if (filter.name.isNullOrEmpty()) filter.name else "%${filter.name}%",
                if (filter.season.isNullOrEmpty()) filter.season else "%${filter.season}%"))
        Episodes(list, Info(1))
    }

    override suspend fun getEpisodeById(id: Int): Episode = try {
        rickAndMortyApi.getEpisodeById(id)
    } catch (e: Exception) {
        EntityToEpisodeMapper.mapOne(databaseStorage.rickAndMortyDao.getEpisodeById(id))
    }

    override suspend fun getEpisodesById(id: String): List<Episode> = try {
        rickAndMortyApi.getEpisodesById(id)
    } catch (e: Exception) {
        EntityToEpisodeMapper.map(databaseStorage.rickAndMortyDao.getEpisodesById(id))
    }

}