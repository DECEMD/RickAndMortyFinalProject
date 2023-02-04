package com.example.cleanarchtest.di.othertodelete

import android.content.Context
import com.example.cleanarchtest.presentation.characters.components.CharacterFragmentComponent
import com.example.cleanarchtest.presentation.characters.components.CharactersFragmentComponent
import com.example.cleanarchtest.presentation.episodes.components.EpisodeFragmentComponent
import com.example.cleanarchtest.presentation.episodes.components.EpisodesFragmentComponent
import com.example.cleanarchtest.presentation.locations.components.LocationFragmentComponent
import com.example.cleanarchtest.presentation.locations.components.LocationsFragmentComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataModule::class])
interface AppComponent {

    val context: Context

    val charactersFragmentComponent: CharactersFragmentComponent
    val characterFragmentComponent: CharacterFragmentComponent

    val locationsFragmentComponent: LocationsFragmentComponent
    val locationFragmentComponent: LocationFragmentComponent

    val episodesFragmentComponent: EpisodesFragmentComponent
    val episodeFragmentComponent: EpisodeFragmentComponent

}