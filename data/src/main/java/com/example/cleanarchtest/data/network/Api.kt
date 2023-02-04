package com.example.cleanarchtest.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import com.example.cleanarchtest.domain.entities.Character
import com.example.cleanarchtest.domain.entities.Characters
import com.example.cleanarchtest.domain.entities.Episode
import com.example.cleanarchtest.domain.entities.Episodes
import com.example.cleanarchtest.domain.entities.Location
import com.example.cleanarchtest.domain.entities.Locations

interface Api {

    @GET("character")
    suspend fun getAllCharacters(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("status") status: String?,
        @Query("gender") gender: String?,
        @Query("species") species: String?
    ): Characters

    @GET("character/{id}")
    suspend fun getCharacterById(
        @Path("id") id: Int
    ): Character

    @GET("character/{id}")
    suspend fun getCharactersById(
        @Path("id") id: String
    ): List<Character>

    @GET("location")
    suspend fun getAllLocations(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("type") type: String?
    ): Locations

    @GET("location/{id}")
    suspend fun getLocationById(
        @Path("id") id: Int
    ): Location

    @GET("location/{id}")
    suspend fun getLocationsById(
        @Path("id") id: String
    ): List<Location>

    @GET("episode")
    suspend fun getAllEpisodes(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("episode") season: String?
    ): Episodes

    @GET("episode/{id}")
    suspend fun getEpisodeById(
        @Path("id") id: Int
    ): Episode

    @GET("episode/{id}")
    suspend fun getEpisodesById(
        @Path("id") id: String
    ): List<Episode>

}