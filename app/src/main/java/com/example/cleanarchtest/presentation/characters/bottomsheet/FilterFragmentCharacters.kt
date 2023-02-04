package com.example.cleanarchtest.presentation.characters.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.example.cleanarchtest.R
import com.example.cleanarchtest.databinding.FragmentFilterCharactersBinding
import com.example.cleanarchtest.domain.entities.CharactersFilter
import com.example.cleanarchtest.presentation.characters.CharactersFragment
import com.example.cleanarchtest.presentation.characters.utils.Constants.BUNDLE_FROM_FILTER_FRAGMENT
import com.example.cleanarchtest.presentation.characters.utils.Constants.DATA_FROM_FILTER_FRAGMENT
import com.example.cleanarchtest.presentation.fragments.base.BottomSheetFragmentBase
import com.example.cleanarchtest.presentation.fragments.base.BottomSheetModel

open class FilterFragmentCharacters :
    BottomSheetFragmentBase<FragmentFilterCharactersBinding>(R.layout.fragment_filter_characters) {
    override var bottomSheetModel: BottomSheetModel = BottomSheetModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.name.setText(CharactersFragment.filter.name)
        binding.status.setSelection(requireContext().resources.getStringArray(R.array.statuses)
            .indexOf(CharactersFragment.filter.status))
        binding.gender.setSelection(requireContext().resources.getStringArray(R.array.genders)
            .indexOf(CharactersFragment.filter.gender))
        binding.species.setSelection(requireContext().resources.getStringArray(R.array.species)
            .indexOf(CharactersFragment.filter.species))

        binding.confirmButton.setOnClickListener {
            val tempFilter = CharactersFilter().apply {
                name = if (binding.name.text.toString().isNotEmpty()) binding.name.text.toString()
                else null
                status = if (binding.status.selectedItem.toString() != "Any") {
                    binding.status.selectedItem as String
                } else null
                gender = if (binding.gender.selectedItem.toString() != "Any") {
                    binding.gender.selectedItem as String
                } else null
                species = if (binding.species.selectedItem.toString() != "Any") {
                    binding.species.selectedItem as String
                } else null
            }

            val result = Bundle().also {
                it.putParcelable(BUNDLE_FROM_FILTER_FRAGMENT, tempFilter)
            }
            parentFragmentManager.setFragmentResult(DATA_FROM_FILTER_FRAGMENT, result)
            dialog?.dismiss()
        }
    }

    override fun createBinding(view: View): FragmentFilterCharactersBinding {
        return FragmentFilterCharactersBinding.bind(view)
    }

    companion object {
        fun show(activity: FragmentActivity) {
            val bottomSheet = FilterFragmentCharacters()
            bottomSheet.show(activity.supportFragmentManager,
                createTag(FilterFragmentCharacters::class.java))
        }

        private fun createTag(clazz: Class<*>): String {
            return clazz.name + "@" + clazz.hashCode()
        }
    }
}