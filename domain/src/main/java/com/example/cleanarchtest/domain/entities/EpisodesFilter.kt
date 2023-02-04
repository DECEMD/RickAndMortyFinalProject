package com.example.cleanarchtest.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class EpisodesFilter(
    var name: String? = null,
    var season: String? = null
) : Parcelable