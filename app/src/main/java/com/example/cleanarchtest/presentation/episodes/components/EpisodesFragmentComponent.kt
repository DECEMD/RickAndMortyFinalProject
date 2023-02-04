package com.example.cleanarchtest.presentation.episodes.components

import com.example.cleanarchtest.presentation.episodes.EpisodesFragment
import dagger.Subcomponent

@Subcomponent
interface EpisodesFragmentComponent {
    fun inject(episodeFragment: EpisodesFragment)
}