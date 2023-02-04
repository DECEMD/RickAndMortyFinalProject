package com.example.cleanarchtest.presentation.episodes.components

import com.example.cleanarchtest.presentation.episodes.EpisodeFragment
import dagger.Subcomponent

@Subcomponent
interface EpisodeFragmentComponent {
    fun inject(episodeFragment: EpisodeFragment)
}