package com.example.cleanarchtest.presentation.episodes.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cleanarchtest.domain.Interactors.CharacterInteractor
import com.example.cleanarchtest.domain.Interactors.IEpisodeInteractor
import com.example.cleanarchtest.domain.entities.Episode
import com.example.cleanarchtest.domain.entities.Character
import com.example.cleanarchtest.presentation.fragments.base.BaseViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class EpisodeViewModel(
    private val characterInteractor: CharacterInteractor,
    private val episodeInteractor: IEpisodeInteractor
): BaseViewModel() {

    private val _charactersMutableLiveData: MutableLiveData<List<Character>> = MutableLiveData()
    val charactersLiveData: LiveData<List<Character>> = _charactersMutableLiveData

    private val _isErrorMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val isErrorLiveData: LiveData<Boolean> = _isErrorMutableLiveData

    private val _isLoadingMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val isLoadingLiveData: LiveData<Boolean> = _isLoadingMutableLiveData

    fun loadCharacters(charactersList: List<String>) {
        isLoadingLiveData(true)
        viewModelScope.launch {
            var characters = ""
            for (character in charactersList) {
                val id = character.split("/").last()
                characters += "$id,"
            }
            val charactersEntityResult = characterInteractor.getCharactersById(characters)
            updateAppropriateLiveData(charactersEntityResult)
        }
    }

    fun loadEpisode(episodeId: Int): Episode {
        var episode: Episode? = null
        runBlocking {
            val getEpisode = async {
                episodeInteractor.getEpisodeById(episodeId)
            }
            episode = getEpisode.await()

        }
        return episode!!
    }

    private fun updateAppropriateLiveData(characters: List<Character>) {
        if (characters.isNotEmpty()) {
            onResultSuccess(characters)
        } else {
            onResultError()
        }
    }

    private fun onResultSuccess(characters: List<Character>) {
        _charactersMutableLiveData.value = characters

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