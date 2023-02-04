package com.example.cleanarchtest.presentation.locations.components

import com.example.cleanarchtest.presentation.locations.LocationsFragment
import dagger.Subcomponent

@Subcomponent
interface LocationsFragmentComponent {
    fun inject(locationsFragment: LocationsFragment)
}