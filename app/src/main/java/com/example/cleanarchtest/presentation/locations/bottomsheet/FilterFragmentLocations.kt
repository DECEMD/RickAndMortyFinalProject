package com.example.cleanarchtest.presentation.locations.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.example.cleanarchtest.R
import com.example.cleanarchtest.databinding.FragmentFilterCharactersBinding
import com.example.cleanarchtest.databinding.FragmentFilterLocationsBinding
import com.example.cleanarchtest.domain.entities.CharactersFilter
import com.example.cleanarchtest.domain.entities.LocationsFilter
import com.example.cleanarchtest.presentation.characters.CharactersFragment
import com.example.cleanarchtest.presentation.locations.utils.Constants.BUNDLE_FROM_FILTER_FRAGMENT
import com.example.cleanarchtest.presentation.locations.utils.Constants.DATA_FROM_FILTER_FRAGMENT
import com.example.cleanarchtest.presentation.fragments.base.BottomSheetFragmentBase
import com.example.cleanarchtest.presentation.fragments.base.BottomSheetModel
import com.example.cleanarchtest.presentation.locations.LocationsFragment

open class FilterFragmentLocations :
    BottomSheetFragmentBase<FragmentFilterLocationsBinding>(R.layout.fragment_filter_locations) {
    override var bottomSheetModel: BottomSheetModel = BottomSheetModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.name.setText(LocationsFragment.filter.name)
        binding.type.setSelection(
            requireContext().resources.getStringArray(R.array.location_types)
                .indexOf(LocationsFragment.filter.type)
        )

        binding.confirmButton.setOnClickListener {
            val tempFilter = LocationsFilter().apply {
                name = if (binding.name.text.toString().isNotEmpty())
                    binding.name.text.toString()
                else null
                type = if (binding.type.selectedItem.toString() != "Any") {
                    binding.type.selectedItem as String
                } else null
            }
            val result = Bundle().also {
                it.putParcelable(BUNDLE_FROM_FILTER_FRAGMENT, tempFilter)
            }
            parentFragmentManager.setFragmentResult(DATA_FROM_FILTER_FRAGMENT, result)
            dialog?.dismiss()
        }
    }

    override fun createBinding(view: View): FragmentFilterLocationsBinding {
        return FragmentFilterLocationsBinding.bind(view)
    }

    companion object {
        fun show(activity: FragmentActivity) {
            val bottomSheet = FilterFragmentLocations()
            bottomSheet.show(activity.supportFragmentManager,
                createTag(FilterFragmentLocations::class.java))
        }

        private fun createTag(clazz: Class<*>): String {
            return clazz.name + "@" + clazz.hashCode()
        }
    }
}