package com.example.cleanarchtest.presentation.locations.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cleanarchtest.domain.Interactors.ILocationInteractor
import javax.inject.Inject

class LocationsViewModelFactory @Inject constructor(
    val locationInteractor: ILocationInteractor
    ): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LocationsViewModel(locationInteractor) as T
    }
}