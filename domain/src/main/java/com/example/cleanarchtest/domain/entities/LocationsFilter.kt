package com.example.cleanarchtest.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class LocationsFilter(
    var name: String? = null,
    var type: String? = null,
) : Parcelable