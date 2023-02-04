package com.example.cleanarchtest.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class CharactersFilter(
    var name: String? = null,
    var status: String? = null,
    var gender: String? = null,
    var species: String? = null,
) : Parcelable