package com.example.cleanarchtest.presentation.episodes.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.example.cleanarchtest.R
import com.example.cleanarchtest.databinding.FragmentFilterCharactersBinding
import com.example.cleanarchtest.databinding.FragmentFilterEpisodesBinding
import com.example.cleanarchtest.domain.entities.CharactersFilter
import com.example.cleanarchtest.domain.entities.EpisodesFilter
import com.example.cleanarchtest.presentation.characters.CharactersFragment
import com.example.cleanarchtest.presentation.episodes.utils.Constants.BUNDLE_FROM_FILTER_FRAGMENT
import com.example.cleanarchtest.presentation.episodes.utils.Constants.DATA_FROM_FILTER_FRAGMENT
import com.example.cleanarchtest.presentation.episodes.EpisodesFragment
import com.example.cleanarchtest.presentation.fragments.base.BottomSheetFragmentBase
import com.example.cleanarchtest.presentation.fragments.base.BottomSheetModel

open class FilterFragmentEpisodes :
    BottomSheetFragmentBase<FragmentFilterEpisodesBinding>(R.layout.fragment_filter_episodes) {
    override var bottomSheetModel: BottomSheetModel = BottomSheetModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.name.setText(EpisodesFragment.filter.name)
        binding.season.setSelection(
            requireContext().resources.getStringArray(R.array.seasons)
                .indexOf(EpisodesFragment.filter.season?.split("0")?.last())
        )

        binding.buttonConfirm.setOnClickListener {
            val tempFilter = EpisodesFilter().apply {
                name = if (binding.name.text.toString().isNotEmpty())
                    binding.name.text.toString()
                else null
                season = if (binding.season.selectedItem.toString() != "All") {
                    "S0${binding.season.selectedItem}"
                } else null
            }
            val result = Bundle().also {
                it.putParcelable(BUNDLE_FROM_FILTER_FRAGMENT, tempFilter)
            }
            parentFragmentManager.setFragmentResult(DATA_FROM_FILTER_FRAGMENT, result)
            dialog?.dismiss()
        }
    }

    override fun createBinding(view: View): FragmentFilterEpisodesBinding {
        return FragmentFilterEpisodesBinding.bind(view)
    }

    companion object {
        fun show(activity: FragmentActivity) {
            val bottomSheet = FilterFragmentEpisodes()
            bottomSheet.show(activity.supportFragmentManager,
                createTag(FilterFragmentEpisodes::class.java))
        }

        private fun createTag(clazz: Class<*>): String {
            return clazz.name + "@" + clazz.hashCode()
        }
    }
}