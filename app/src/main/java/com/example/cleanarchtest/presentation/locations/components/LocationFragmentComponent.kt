package com.example.cleanarchtest.presentation.locations.components

import com.example.cleanarchtest.presentation.locations.LocationFragment
import dagger.Subcomponent

@Subcomponent
interface LocationFragmentComponent {
    fun inject(locationFragment: LocationFragment)
}