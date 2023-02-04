package com.example.cleanarchtest.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cleanarchtest.data.storage.dao.RickAndMortyDao
import com.example.cleanarchtest.domain.entities.CharacterEntity
import com.example.cleanarchtest.domain.entities.EpisodeEntity
import com.example.cleanarchtest.domain.entities.LocationEntity

@Database(
    entities = [CharacterEntity::class, LocationEntity::class, EpisodeEntity::class],
    version = 1
)
abstract class DatabaseStorage : RoomDatabase() {

    abstract val rickAndMortyDao: RickAndMortyDao

    companion object {
        const val RICK_AND_MORTY_DATA_BASE = "RICK_AND_MORTY_DATA_BASE"
    }

}