package com.example.cleanarchtest.presentation.fragments

interface OnFragmentToStart {

    fun onStartCharacterListFragment()
//    fun onStartCharacterFilterFragment()
    fun onStartCharacterDetailsFragment(characterId: Int)
//    fun onStartFilteredCharacterListFragment(
//        status: String?,
//        gender: String?,
//        species: String?,
//        type: String?
//    )

    fun onStartLocationListFragment()
//    fun onStartLocationFilterFragment()
    fun onStartLocationDetailsFragment(locationId: Int)
//    fun onStartFilteredLocationListFragment(type: String?, dimension: String?)

    fun onStartEpisodeListFragment()
//    fun onStartEpisodeFilterFragment()
    fun onStartEpisodeDetailsFragment(episodeId: Int)
//    fun onStartFilteredEpisodeListFragment(episode: String?)

    fun onBackButton()


}
