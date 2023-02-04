package com.example.cleanarchtest.presentation.characters.components

import com.example.cleanarchtest.presentation.characters.CharactersFragment
import dagger.Subcomponent

@Subcomponent
interface CharactersFragmentComponent {
    fun inject(charactersFragment: CharactersFragment)
}

//modules = [CharactersFragmentModule::class]

//@Module
//interface CharactersFragmentModule {
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(CharactersViewModel::class)
//    fun bindViewModel(viewModel: CharactersViewModel): ViewModel
//
//}