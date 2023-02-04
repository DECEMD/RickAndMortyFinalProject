package com.example.cleanarchtest.presentation.locations.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cleanarchtest.domain.Interactors.CharacterInteractor
import com.example.cleanarchtest.domain.Interactors.ILocationInteractor
import javax.inject.Inject

class LocationViewModelFactory @Inject constructor(
    val characterInteractor: CharacterInteractor,
    val locationInteractor: ILocationInteractor
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LocationViewModel(characterInteractor, locationInteractor) as T
    }
}