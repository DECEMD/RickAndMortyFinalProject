package com.example.cleanarchtest.presentation.episodes.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cleanarchtest.domain.Interactors.IEpisodeInteractor
import javax.inject.Inject

class EpisodesViewModelFactory @Inject constructor(
    val episodeInteractor: IEpisodeInteractor
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EpisodesViewModel(episodeInteractor) as T
    }
}