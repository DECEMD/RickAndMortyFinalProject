package com.example.cleanarchtest.presentation.characters.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cleanarchtest.domain.Interactors.CharacterInteractor
import javax.inject.Inject

class CharactersViewModelFactory @Inject constructor(val characterInteractor: CharacterInteractor): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CharactersViewModel(characterInteractor) as T
    }
}