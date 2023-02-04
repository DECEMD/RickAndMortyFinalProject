package com.example.cleanarchtest.presentation.characters.components

import com.example.cleanarchtest.presentation.characters.CharacterFragment
import dagger.Subcomponent

@Subcomponent
interface CharacterFragmentComponent {
    fun inject(characterFragment: CharacterFragment)
}