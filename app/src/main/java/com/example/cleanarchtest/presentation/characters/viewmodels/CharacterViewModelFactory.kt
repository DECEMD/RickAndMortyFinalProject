package com.example.cleanarchtest.presentation.characters.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cleanarchtest.domain.Interactors.CharacterInteractor
import com.example.cleanarchtest.domain.Interactors.IEpisodeInteractor
import javax.inject.Inject

class CharacterViewModelFactory @Inject constructor(
    val characterInteractor: CharacterInteractor,
    val episodeInteractor: IEpisodeInteractor): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CharacterViewModel(characterInteractor, episodeInteractor) as T
    }
}