package com.example.cleanarchtest.presentation.episodes

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cleanarchtest.R
import com.example.cleanarchtest.app.App
import com.example.cleanarchtest.databinding.FragmentEpisodeBinding
import com.example.cleanarchtest.domain.Interactors.CharacterInteractor
import com.example.cleanarchtest.domain.Interactors.IEpisodeInteractor
import com.example.cleanarchtest.domain.entities.Episode
import com.example.cleanarchtest.domain.entities.Character
import com.example.cleanarchtest.onNavigate
import com.example.cleanarchtest.presentation.episodes.adapters.EpisodeAdapter
import com.example.cleanarchtest.presentation.episodes.viewmodels.EpisodeViewModel
import com.example.cleanarchtest.presentation.episodes.viewmodels.EpisodeViewModelFactory
import com.example.cleanarchtest.presentation.fragments.base.BaseFragment
import javax.inject.Inject

class EpisodeFragment: BaseFragment<FragmentEpisodeBinding>(R.layout.fragment_episode) {
    override fun createBinding(view: View): FragmentEpisodeBinding {
        return FragmentEpisodeBinding.bind(view)
    }

    @Inject
    lateinit var characterInteractor: CharacterInteractor

    @Inject
    lateinit var episodeInteractor: IEpisodeInteractor

    @Inject
    lateinit var vmFactory: EpisodeViewModelFactory
    private lateinit var vm: EpisodeViewModel

    private var characters: List<Character> = emptyList()
    private val adapter = EpisodeAdapter()
    private var episode: Episode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as App).appComponent.episodeFragmentComponent.inject(this)
        vm = ViewModelProvider(this, vmFactory)[EpisodeViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onNavigate().onBackButton()
        if (episode == null) {
            val episodeId = arguments?.getInt(EPISODE_ID)
            episode = vm.loadEpisode(episodeId!!)
            episode?.characters?.let { vm.loadCharacters(it) }
        }
        initViews()
        observeLiveData()
    }

    private fun initViews() = with(binding) {
        episodeCode.text = episode?.episode
        episodeName.text = episode?.name
        episodeDate.text = episode?.date

        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter.onClick = { character ->
            Log.d("log","episode click")
            onNavigate().onStartCharacterDetailsFragment(character.id)
        }
        binding.episodeRecyclerView.apply {
            adapter = this@EpisodeFragment.adapter
            this@EpisodeFragment.adapter.setData(characters)
        }
    }

    private fun observeLiveData() {
        vm.charactersLiveData.observe(requireActivity(), Observer(::onCharactersReceived))
        vm.isErrorLiveData.observe(requireActivity()) { onErrorReceived() }
        vm.isLoadingLiveData.observe(requireActivity(), Observer(::onLoadingStateReceived))
    }

    private fun onCharactersReceived(characters: List<Character>) {
        this.characters = characters
        adapter.updateAdapter(characters)
    }

    private fun onErrorReceived() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.network_connection_error_title)
            .setCancelable(false)
            .setNegativeButton(R.string.network_connection_error_cancel) { dialog, _ -> dialog.cancel() }
            .setPositiveButton(R.string.network_connection_error_action) { _, _ ->
                episode?.characters?.let {
                    vm.loadCharacters(
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
        fun newInstance(episodeId: Int): EpisodeFragment {
            val args = Bundle().apply {
                putInt(EPISODE_ID, episodeId)
            }
            return EpisodeFragment().apply {
                arguments = args
            }
        }
        const val EPISODE_ID = "EPISODE_ID"
    }
}