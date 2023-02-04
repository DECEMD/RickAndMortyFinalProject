package com.example.cleanarchtest.presentation.locations

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cleanarchtest.R
import com.example.cleanarchtest.app.App
import com.example.cleanarchtest.databinding.FragmentLocationBinding
import com.example.cleanarchtest.domain.Interactors.CharacterInteractor
import com.example.cleanarchtest.domain.Interactors.ILocationInteractor
import com.example.cleanarchtest.presentation.fragments.base.BaseFragment
import com.example.cleanarchtest.domain.entities.Character
import com.example.cleanarchtest.domain.entities.Location
import com.example.cleanarchtest.onNavigate
import com.example.cleanarchtest.presentation.locations.adapters.LocationAdapter
import com.example.cleanarchtest.presentation.locations.viewmodels.LocationViewModel
import com.example.cleanarchtest.presentation.locations.viewmodels.LocationViewModelFactory
import javax.inject.Inject

class LocationFragment: BaseFragment<FragmentLocationBinding>(R.layout.fragment_location) {

    @Inject
    lateinit var charactersInteractor: CharacterInteractor

    @Inject
    lateinit var locationsInteractor: ILocationInteractor

    @Inject
    lateinit var vmFactory: LocationViewModelFactory
    private lateinit var vm: LocationViewModel

    private val adapter = LocationAdapter()
    private var characters: List<Character> = emptyList()
    private var location: Location? = null


    override fun createBinding(view: View): FragmentLocationBinding {
        return FragmentLocationBinding.bind(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as App).appComponent.locationFragmentComponent.inject(this)
        vm = ViewModelProvider(this, vmFactory)[LocationViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onNavigate().onBackButton()
        if (location == null) {
            val locationId = arguments?.getInt(LOCATION_ID)
            location = vm.loadLocation(locationId!!)
            location?.characters?.let { vm.loadCharacters(it) }
        }
        initViews()
        observeLiveData()
    }

    private fun initViews() = with(binding) {
        locationName.text = location?.name
        locationDimension.text = location?.dimension
        locationType.text = location?.type
        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter.onClick = { character ->
            onNavigate().onStartCharacterDetailsFragment(character.id)
        }
        binding.episodeRecyclerView.apply {
            adapter = this@LocationFragment.adapter
            this@LocationFragment.adapter.setData(characters)
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
                location?.characters?.let {
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
        fun newInstance(locationId: Int): LocationFragment {
            val args = Bundle().apply {
                putInt(LOCATION_ID, locationId)
            }
            return LocationFragment().apply {
                arguments = args
            }
        }
        private const val LOCATION_ID = "LOCATION_ID"
    }
}