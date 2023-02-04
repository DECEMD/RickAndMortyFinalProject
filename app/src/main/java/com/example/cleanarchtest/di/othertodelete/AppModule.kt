package com.example.cleanarchtest.di.othertodelete

import android.content.Context
import androidx.room.Room
import com.example.cleanarchtest.data.RepositoryImplementation
import com.example.cleanarchtest.data.storage.DatabaseStorage
import com.example.cleanarchtest.domain.Interactors.CharacterInteractor
import com.example.cleanarchtest.domain.Interactors.CharacterInteractorInterface
import com.example.cleanarchtest.domain.Interactors.EpisodeInteractor
import com.example.cleanarchtest.domain.Interactors.IEpisodeInteractor
import com.example.cleanarchtest.domain.Interactors.ILocationInteractor
import com.example.cleanarchtest.domain.Interactors.LocationInteractor
import com.example.cleanarchtest.domain.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [AppModule.InnerAppModule::class])
class AppModule(private val context: Context) {

    @Provides
    fun provideContext(): Context {
        return context
    }

    @Singleton
    @Provides
    fun provideDatabaseStorage() =
        Room.databaseBuilder(
            context,
            DatabaseStorage::class.java,
            DatabaseStorage.RICK_AND_MORTY_DATA_BASE
        ).build()

    @Module
    interface InnerAppModule {

        @Binds
        @Singleton
        fun provideRepository(repository: RepositoryImplementation): Repository

        @Singleton
        @Binds
        fun provideCharacterInteractor(characterInteractor: CharacterInteractor): CharacterInteractorInterface

        @Singleton
        @Binds
        fun provideEpisodeInteractor(episodeInteractor: EpisodeInteractor): IEpisodeInteractor

        @Singleton
        @Binds
        fun provideLocationInteractor(locationInteractor: LocationInteractor): ILocationInteractor

    }
}