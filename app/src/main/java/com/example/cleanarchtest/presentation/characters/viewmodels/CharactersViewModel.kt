package com.example.cleanarchtest.presentation.characters.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cleanarchtest.domain.Interactors.CharacterInteractor
import com.example.cleanarchtest.domain.entities.Character
import com.example.cleanarchtest.domain.entities.CharactersFilter
import com.example.cleanarchtest.presentation.characters.CharactersFragment
import com.example.cleanarchtest.presentation.fragments.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CharactersViewModel(private val characterInteractor: CharacterInteractor): BaseViewModel() {

    private val _charactersMutableLiveData: MutableLiveData<List<Character>> = MutableLiveData()
    val charactersLiveData: LiveData<List<Character>> = _charactersMutableLiveData

    private val _isErrorMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val isErrorLiveData: LiveData<Boolean> = _isErrorMutableLiveData

    private val _isLoadingMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val isLoadingLiveData: LiveData<Boolean> = _isLoadingMutableLiveData

    fun loadCharacters(page: Int, filter: CharactersFilter) {
        isLoadingLiveData(true)
        viewModelScope.launch {
            val charactersEntityResult = characterInteractor.getAllCharacters(page, filter)
            Log.d("log", "${characterInteractor.getAllCharacters(page, filter)}")
            CharactersFragment.pages = charactersEntityResult.info.pages
            updateAppropriateLiveData(charactersEntityResult.results, 0)
        }
    }

    fun reloadCharacters(filter: CharactersFilter) {
        isLoadingLiveData(true)
        viewModelScope.launch {
            val charactersEntityResult = characterInteractor.getAllCharacters(1, filter)
            CharactersFragment.pages = charactersEntityResult.info.pages
            updateAppropriateLiveData(charactersEntityResult.results, 1)
        }
    }

    private fun updateAppropriateLiveData(characters: List<Character>, flag: Int) {
        if (characters.isNotEmpty()) {
            onResultSuccess(characters, flag)
        } else {
            onResultError()
        }
    }

    private fun onResultSuccess(characters: List<Character>, flag: Int) {
        when (flag) {
            0 ->
                if (!_charactersMutableLiveData.value.isNullOrEmpty()) {
                    _charactersMutableLiveData.value =
                        _charactersMutableLiveData.value?.plus(characters)
                } else {
                    _charactersMutableLiveData.value = characters
                }
            1 -> _charactersMutableLiveData.value = characters
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