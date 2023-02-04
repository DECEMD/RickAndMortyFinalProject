package com.example.cleanarchtest.presentation.locations.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cleanarchtest.domain.Interactors.ILocationInteractor
import com.example.cleanarchtest.domain.entities.Location
import com.example.cleanarchtest.domain.entities.LocationsFilter
import com.example.cleanarchtest.presentation.fragments.base.BaseViewModel
import com.example.cleanarchtest.presentation.locations.LocationsFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LocationsViewModel(private val locationInteractor: ILocationInteractor): BaseViewModel() {

    private val _locationsMutableLiveData: MutableLiveData<List<Location>> = MutableLiveData()
    val locationsLiveData: LiveData<List<Location>> = _locationsMutableLiveData

    private val _isErrorMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val isErrorLiveData: LiveData<Boolean> = _isErrorMutableLiveData

    private val _isLoadingMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val isLoadingLiveData: LiveData<Boolean> = _isLoadingMutableLiveData

    fun loadLocations(page: Int, filter: LocationsFilter) {
        isLoadingLiveData(true)
        viewModelScope.launch {
            val locationsEntityResult = locationInteractor.getAllLocations(page, filter)
            LocationsFragment.pages = locationsEntityResult.info.pages
            updateAppropriateLiveData(locationsEntityResult.results, 0)
        }
    }

    fun reloadLocations(filter: LocationsFilter) {
        isLoadingLiveData(true)
        viewModelScope.launch {
            val locationsEntityResult = locationInteractor.getAllLocations(1, filter)
            LocationsFragment.pages = locationsEntityResult.info.pages
            updateAppropriateLiveData(locationsEntityResult.results, 1)
        }
    }

    private fun updateAppropriateLiveData(locations: List<Location>, flag: Int) {
        if (locations.isNotEmpty()) {
            onResultSuccess(locations, flag)
        } else {
            onResultError()
        }
    }

    private fun onResultSuccess(locations: List<Location>, flag: Int) {
        when (flag) {
            0 ->
                if (!_locationsMutableLiveData.value.isNullOrEmpty()) {
                    _locationsMutableLiveData.value = _locationsMutableLiveData.value?.plus(locations)
                } else {
                    _locationsMutableLiveData.value = locations
                }
            1 -> _locationsMutableLiveData.value = locations
        }
        isLoadingLiveData(false)
    }

    private fun onResultError() {
        viewModelScope.launch {
            delay(300)
            isLoadingLiveData(false)
        }.invokeOnCompletion {
            _isErrorMutableLiveData.value = true
        }
    }

    private fun isLoadingLiveData(isLoading: Boolean) {
        this._isLoadingMutableLiveData.value = isLoading
    }

}