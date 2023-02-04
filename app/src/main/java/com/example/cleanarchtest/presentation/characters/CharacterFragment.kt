package com.example.cleanarchtest.presentation.characters

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.cleanarchtest.R
import com.example.cleanarchtest.app.App
import com.example.cleanarchtest.databinding.FragmentCharacterBinding
import com.example.cleanarchtest.domain.Interactors.CharacterInteractor
import com.example.cleanarchtest.domain.Interactors.IEpisodeInteractor
import com.example.cleanarchtest.domain.entities.Episode
import com.example.cleanarchtest.domain.entities.Character
import com.example.cleanarchtest.makeToast
import com.example.cleanarchtest.onNavigate
import com.example.cleanarchtest.presentation.characters.adapters.CharacterAdapter
import com.example.cleanarchtest.presentation.characters.viewmodels.CharacterViewModel
import com.example.cleanarchtest.presentation.characters.viewmodels.CharacterViewModelFactory
import com.example.cleanarchtest.presentation.fragments.base.BaseFragment
import javax.inject.Inject

class CharacterFragment: BaseFragment<FragmentCharacterBinding>(R.layout.fragment_character) {

    override fun createBinding(view: View): FragmentCharacterBinding {
        return FragmentCharacterBinding.bind(view)
    }

    @Inject
    lateinit var episodeInteractor: IEpisodeInteractor

    @Inject
    lateinit var characterInteractor: CharacterInteractor

    @Inject
    lateinit var vmFactory: CharacterViewModelFactory
    private lateinit var vm: CharacterViewModel

    private var episodes: List<Episode> = emptyList()
    private var character: Character? = null
    private val adapter = CharacterAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as App).appComponent.characterFragmentComponent.inject(this)
        vm = ViewModelProvider(this, vmFactory)[CharacterViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onNavigate().onBackButton()
        if (character == null) {
            val characterId = arguments?.getInt(CHARACTER_ID)
            character = vm.loadCharacter(characterId!!)
            character?.episode?.let { vm.loadEpisodes(it) }
        }
        initViews()
        observeLiveData()
    }

    private fun initViews() = with(binding) {
        characterName.text = character?.name
        characterSpecies.text = character?.species
        characterType.text = if (character?.type!!.isEmpty()) "None" else character?.type
        characterStatus.text = character?.status
        characterGender.text = character?.gender

        characterLocation.text = character?.location?.name
        characterLocation.setOnClickListener {
            try {
                onNavigate().onStartLocationDetailsFragment(character?.location?.url!!.split("/")
                    .last().toInt())
            } catch (e: NumberFormatException) {
                requireContext().makeToast(getString(R.string.character_no_location_error))
            }
        }

        characterOrigin.text = character?.origin?.name
        characterOrigin.setOnClickListener {
            try {
                onNavigate().onStartLocationDetailsFragment(character?.origin?.url!!.split("/")
                    .last().toInt())
            } catch (e: NumberFormatException) {
                requireContext().makeToast(getString(R.string.character_no_origin_error))
            }
        }

        Glide
            .with(requireContext())
            .load(character?.image)
            .into(characterImage)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter.onClick = { episode ->
            onNavigate().onStartEpisodeDetailsFragment(episode.id)
        }
        binding.recyclerViewEpisodes.apply {
            adapter = this@CharacterFragment.adapter
            this@CharacterFragment.adapter.setData(episodes)
        }
    }

    private fun observeLiveData() {
        vm.episodesLiveData.observe(requireActivity(), Observer(::onEpisodesReceived))
        vm.isErrorLiveData.observe(requireActivity()) { onErrorReceived() }
        vm.isLoadingLiveData.observe(requireActivity(), Observer(::onLoadingStateReceived))
    }

    private fun onEpisodesReceived(episodes: List<Episode>) {
        this.episodes = episodes
        adapter.updateAdapter(episodes)
    }

    private fun onErrorReceived() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.network_connection_error_title)
            .setCancelable(false)
            .setNegativeButton(R.string.network_connection_error_cancel) { dialog, _ -> dialog.cancel() }
            .setPositiveButton(R.string.network_connection_error_action) { _, _ ->
                character?.episode?.let {
                    vm.loadEpisodes(
                        it
                    )
                }
            }
            .show()
    }

    private fun onLoadingStateReceived(isLoading: Boolean) {
        showSpinner(isLoading)
    }

    private fun showSpinner(isLoading: Boolean) {
        binding.progressBar.apply {
            visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    companion object {
        fun newInstance(characterId: Int): CharacterFragment {
            val args = Bundle().apply {
                putInt(CHARACTER_ID, characterId)
            }
            return CharacterFragment().apply {
                arguments = args
            }
        }
        private const val CHARACTER_ID = "CHARACTER_ID"
    }
}