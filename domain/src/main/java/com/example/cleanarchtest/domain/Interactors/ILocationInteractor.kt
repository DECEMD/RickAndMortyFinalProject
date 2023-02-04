package com.example.cleanarchtest.domain.Interactors

import com.example.cleanarchtest.domain.Repository
import com.example.cleanarchtest.domain.entities.Location
import com.example.cleanarchtest.domain.entities.Locations
import com.example.cleanarchtest.domain.entities.LocationsFilter
import javax.inject.Inject

interface ILocationInteractor {

    suspend fun getAllLocations(page: Int, filter: LocationsFilter): Locations

    suspend fun getLocationById(id: Int): Location

    suspend fun getLocationsById(id: String): List<Location>
}

class LocationInteractor @Inject constructor(private val repository: Repository) :
    ILocationInteractor {

    override suspend fun getAllLocations(page: Int, filter: LocationsFilter) = repository.getAllLocations(page, filter)

    override suspend fun getLocationById(id: Int) = repository.getLocationById(id)

    override suspend fun getLocationsById(id: String) = repository.getLocationsById(id)

}