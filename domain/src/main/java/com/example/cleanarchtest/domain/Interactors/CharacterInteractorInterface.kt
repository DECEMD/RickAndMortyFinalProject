package com.example.cleanarchtest.domain.Interactors

import com.example.cleanarchtest.domain.entities.Character
import com.example.cleanarchtest.domain.Repository
import com.example.cleanarchtest.domain.entities.Characters
import com.example.cleanarchtest.domain.entities.CharactersFilter
import javax.inject.Inject

interface CharacterInteractorInterface {

    suspend fun getAllCharacters(page: Int, filter: CharactersFilter): Characters

    suspend fun getCharacterById(id: Int): Character

    suspend fun getCharactersById(id: String): List<Character>

}

class CharacterInteractor @Inject constructor(private val repository: Repository) :
    CharacterInteractorInterface {

    override suspend fun getAllCharacters(page: Int, filter: CharactersFilter) = repository.getAllCharacters(page, filter)

    override suspend fun getCharacterById(id: Int): Character = repository.getCharacterById(id)

    override suspend fun getCharactersById(id: String): List<Character> = repository.getCharactersById(id)

}