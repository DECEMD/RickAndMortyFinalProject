package com.example.cleanarchtest.presentation.episodes.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cleanarchtest.domain.Interactors.IEpisodeInteractor
import com.example.cleanarchtest.domain.entities.Episode
import com.example.cleanarchtest.domain.entities.EpisodesFilter
import com.example.cleanarchtest.presentation.episodes.EpisodesFragment
import com.example.cleanarchtest.presentation.fragments.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EpisodesViewModel (private val episodeInteractor: IEpisodeInteractor): BaseViewModel() {

    private val _episodesMutableLiveData: MutableLiveData<List<Episode>> = MutableLiveData()
    val episodesLiveData: LiveData<List<Episode>> = _episodesMutableLiveData

    private val _isErrorMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val isErrorLiveData: LiveData<Boolean> = _isErrorMutableLiveData

    private val _isLoadingMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val isLoadingLiveData: LiveData<Boolean> = _isLoadingMutableLiveData

    fun loadEpisodes(page: Int, filter: EpisodesFilter) {
        isLoadingLiveData(true)
        viewModelScope.launch {
            val episodesEntityResult = episodeInteractor.getAllEpisodes(page, filter)
            EpisodesFragment.pages = episodesEntityResult.info.pages
            updateAppropriateLiveData(episodesEntityResult.results, 0)
        }
    }

    fun reloadEpisodes(filter: EpisodesFilter) {
        isLoadingLiveData(true)
        viewModelScope.launch {
            val episodesEntityResult = episodeInteractor.getAllEpisodes(1, filter)
            EpisodesFragment.pages = episodesEntityResult.info.pages
            updateAppropriateLiveData(episodesEntityResult.results, 1)
        }
    }

    private fun updateAppropriateLiveData(episodes: List<Episode>, flag: Int) {
        if (episodes.isNotEmpty()) {
            onResultSuccess(episodes, flag)
        } else {
            onResultError()
        }
    }

    private fun onResultSuccess(episodes: List<Episode>, flag: Int) {
        when (flag) {
            0 ->
                if (!_episodesMutableLiveData.value.isNullOrEmpty()) {
                    _episodesMutableLiveData.value = _episodesMutableLiveData.value?.plus(episodes)
                } else {
                    _episodesMutableLiveData.value = episodes
                }
            1 -> _episodesMutableLiveData.value = episodes
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