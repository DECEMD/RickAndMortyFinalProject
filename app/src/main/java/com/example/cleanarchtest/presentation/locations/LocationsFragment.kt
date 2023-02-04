package com.example.cleanarchtest.presentation.locations

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.cleanarchtest.R
import com.example.cleanarchtest.app.App
import com.example.cleanarchtest.databinding.FragmentLocationsListBinding
import com.example.cleanarchtest.domain.Interactors.ILocationInteractor
import com.example.cleanarchtest.domain.entities.Location
import com.example.cleanarchtest.domain.entities.LocationsFilter
import com.example.cleanarchtest.onNavigate
import com.example.cleanarchtest.presentation.characters.bottomsheet.FilterFragmentCharacters
import com.example.cleanarchtest.presentation.episodes.EpisodesFragment
import com.example.cleanarchtest.presentation.locations.utils.Constants.BUNDLE_FROM_FILTER_FRAGMENT
import com.example.cleanarchtest.presentation.locations.utils.Constants.DATA_FROM_FILTER_FRAGMENT
import com.example.cleanarchtest.presentation.fragments.base.BaseFragment
import com.example.cleanarchtest.presentation.locations.adapters.LocationsAdapter
import com.example.cleanarchtest.presentation.locations.bottomsheet.FilterFragmentLocations
import com.example.cleanarchtest.presentation.locations.viewmodels.LocationsViewModel
import com.example.cleanarchtest.presentation.locations.viewmodels.LocationsViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocationsFragment :
    BaseFragment<FragmentLocationsListBinding>(R.layout.fragment_locations_list) {

    @Inject
    lateinit var locationsInteractor: ILocationInteractor

    private val adapter = LocationsAdapter()
    private var queryTextChangedJob: Job? = null
    private lateinit var searchText: String

    @Inject
    lateinit var vmFactory: LocationsViewModelFactory
    private lateinit var vm: LocationsViewModel

    private var locations = emptyList<Location>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as App).appComponent.locationsFragmentComponent.inject(this)
        vm = ViewModelProvider(this, vmFactory)[LocationsViewModel::class.java]
    }

    override fun createBinding(view: View): FragmentLocationsListBinding {
        return FragmentLocationsListBinding.bind(view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onNavigate().onBackButton()
        hideKeyboard(binding.viewToOperateKeyboard)
        binding.locationsListSearchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                doSearchViewFilter()
                return false
            }

            override fun onQueryTextChange(input: String?): Boolean {
                val text = input ?: return false
                searchText = text

                queryTextChangedJob?.cancel()
                queryTextChangedJob = lifecycleScope.launch(Dispatchers.Main) {
                    delay(1000)
                    doSearchViewFilter()
                }
                return false
            }
        })
        binding.locationsListFilterButton.setOnClickListener {
            FilterFragmentLocations.show(requireActivity())
            parentFragmentManager.setFragmentResultListener(DATA_FROM_FILTER_FRAGMENT,
                this) { _, result ->
                run {
                    filter = result.getParcelable(BUNDLE_FROM_FILTER_FRAGMENT)!!
                    vm.reloadLocations(filter)
                }
            }
        }

        initRecyclerView()
        observeLiveData()
        if (locations.isEmpty()) {
            page = 1
            vm.loadLocations(page, filter)
        }
        binding.locationsListResetFilterButton.setOnClickListener {
            binding.locationsListSearchView.setQuery("", true)
            filter = LocationsFilter(null, null)
            vm.reloadLocations(filter)
        }
        binding.locationsListSwipeToRefreshHolder.setOnRefreshListener {
            binding.locationsListSearchView.setQuery("", true)
            filter.name = null
            vm.reloadLocations(filter)
            binding.locationsListSwipeToRefreshHolder.isRefreshing = false
        }
    }

    private fun doSearchViewFilter() {
        filter.name = binding.locationsListSearchView.query.toString()
        page = 1
        vm.reloadLocations(filter)
    }

    private fun initRecyclerView() {
        adapter.onClick = { location ->
            onNavigate().onStartLocationDetailsFragment(location.id)
        }
        binding.locationsListRecyclerView.apply {
            adapter = this@LocationsFragment.adapter
            this@LocationsFragment.adapter.setData(locations)
        }
        binding.locationsListRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1) and (page < pages)) {
                    page += 1
                    vm.loadLocations(page, filter)
                }
            }
        })
    }

    private fun observeLiveData() {
        vm.locationsLiveData.observe(requireActivity(), Observer(::onLocationsReceived))
        vm.isErrorLiveData.observe(requireActivity()) {
            if (requireActivity().isFinishing) {
                vm.reloadLocations(filter)
            } else {
                onErrorReceived()
            }
        }
        vm.isLoadingLiveData.observe(requireActivity(), Observer(::onLoadingStateReceived))
    }

    private fun onLocationsReceived(locations: List<Location>) {
        this.locations = locations
        adapter.updateAdapter(locations)
    }

    private fun onErrorReceived() {
        AlertDialog.Builder(requireContext()).setTitle(R.string.network_connection_no_data)
            .setCancelable(false)
            .setNegativeButton(R.string.network_connection_error_cancel) { dialog, _ -> dialog.cancel() }
            .setPositiveButton(R.string.network_connection_error_action) { _, _ ->
                vm.reloadLocations(filter)
            }.show()
        filter = LocationsFilter()
    }

    private fun onLoadingStateReceived(isLoading: Boolean) {
        binding.progressBar.apply {
            visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        page = 1
    }

    companion object {
        fun newInstance() = LocationsFragment()
        var page = 1
        var pages = 1
        var filter = LocationsFilter()
    }
}