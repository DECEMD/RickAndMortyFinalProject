package com.example.cleanarchtest.presentation.characters.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cleanarchtest.domain.Interactors.CharacterInteractor
import com.example.cleanarchtest.domain.Interactors.IEpisodeInteractor
import com.example.cleanarchtest.domain.entities.Character
import com.example.cleanarchtest.domain.entities.Episode
import com.example.cleanarchtest.presentation.fragments.base.BaseViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CharacterViewModel(private val characterInteractor: CharacterInteractor,
                         private val episodeInteractor: IEpisodeInteractor
): BaseViewModel() {

    private val _episodesMutableLiveData: MutableLiveData<List<Episode>> = MutableLiveData()
    val episodesLiveData: LiveData<List<Episode>> = _episodesMutableLiveData

    private val _isErrorMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val isErrorLiveData: LiveData<Boolean> = _isErrorMutableLiveData

    private val _isLoadingMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val isLoadingLiveData: LiveData<Boolean> = _isLoadingMutableLiveData

    fun loadEpisodes(episodesList: List<String>) {
        isLoadingLiveData(true)
        viewModelScope.launch {
            var episodes = ""
            for (episode in episodesList) {
                val id = episode.split("/").last()
                episodes += "$id,"
            }
            val episodesEntityResult = episodeInteractor.getEpisodesById(episodes)
            updateAppropriateLiveData(episodesEntityResult)
        }
    }

    fun loadCharacter(characterId: Int): Character {
        var character: Character? = null
        runBlocking {
            val getCharacter = async {
                characterInteractor.getCharacterById(characterId)
            }
            character = getCharacter.await()

        }
        return character!!
    }

    private fun updateAppropriateLiveData(episodes: List<Episode>) {
        if (!episodes.isNullOrEmpty()) {
            onResultSuccess(episodes)
        } else {
            onResultError()
        }
    }

    private fun onResultSuccess(episodes: List<Episode>) {
        _episodesMutableLiveData.value = episodes
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