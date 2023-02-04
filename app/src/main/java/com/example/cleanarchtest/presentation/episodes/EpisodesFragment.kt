package com.example.cleanarchtest.presentation.episodes

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
import com.example.cleanarchtest.databinding.FragmentEpisodeListBinding
import com.example.cleanarchtest.domain.Interactors.IEpisodeInteractor
import com.example.cleanarchtest.domain.entities.Episode
import com.example.cleanarchtest.domain.entities.EpisodesFilter
import com.example.cleanarchtest.onNavigate
import com.example.cleanarchtest.presentation.characters.CharactersFragment
import com.example.cleanarchtest.presentation.characters.utils.Constants
import com.example.cleanarchtest.presentation.episodes.utils.Constants.DATA_FROM_FILTER_FRAGMENT
import com.example.cleanarchtest.presentation.episodes.adapters.EpisodesAdapter
import com.example.cleanarchtest.presentation.episodes.bottomsheet.FilterFragmentEpisodes
import com.example.cleanarchtest.presentation.episodes.utils.Constants.BUNDLE_FROM_FILTER_FRAGMENT
import com.example.cleanarchtest.presentation.episodes.viewmodels.EpisodesViewModel
import com.example.cleanarchtest.presentation.episodes.viewmodels.EpisodesViewModelFactory
import com.example.cleanarchtest.presentation.fragments.base.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class EpisodesFragment: BaseFragment<FragmentEpisodeListBinding>(R.layout.fragment_episode_list){

    override fun createBinding(view: View): FragmentEpisodeListBinding {
        return FragmentEpisodeListBinding.bind(view)
    }

    @Inject
    lateinit var vmFactory: EpisodesViewModelFactory
    private lateinit var vm: EpisodesViewModel

    @Inject
    lateinit var episodesInteractor: IEpisodeInteractor

    private val adapter = EpisodesAdapter()
    private var queryTextChangedJob: Job? = null
    private lateinit var searchText: String

    private var episodes = emptyList<Episode>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as App).appComponent.episodesFragmentComponent.inject(this)
        vm = ViewModelProvider(this, vmFactory)[EpisodesViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onNavigate().onBackButton()
        hideKeyboard(binding.viewToOperateKeyboard)
        binding.episodesListSearchView.setOnQueryTextListener(object:
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
        binding.episodesListFilterButton.setOnClickListener {
            FilterFragmentEpisodes.show(requireActivity())
            parentFragmentManager.setFragmentResultListener(DATA_FROM_FILTER_FRAGMENT,
                this) { _, result ->
                run {
                    filter = result.getParcelable(BUNDLE_FROM_FILTER_FRAGMENT)!!
                    vm.reloadEpisodes(filter)
                }
            }
        }
        initRecyclerView()
        observeLiveData()
        if (episodes.isEmpty()) {
            page = 1
            vm.loadEpisodes(page, filter)
        }
        binding.episodesListResetFilterButton.setOnClickListener {
            binding.episodesListSearchView.setQuery("", true)
            filter = EpisodesFilter(null, null)
            vm.reloadEpisodes(filter)
        }
        binding.episodesListSwipeToRefreshHolder.setOnRefreshListener {
            binding.episodesListSearchView.setQuery("", true)
            filter.name = null
            vm.reloadEpisodes(filter)
            binding.episodesListSwipeToRefreshHolder.isRefreshing = false
        }
    }

    private fun doSearchViewFilter(){
        filter.name = binding.episodesListSearchView.query.toString()
        page = 1
        vm.reloadEpisodes(filter)
    }

    private fun initRecyclerView() {
        adapter.onClick = { episode ->
            onNavigate().onStartEpisodeDetailsFragment(episode.id)
        }
        binding.episodesListRecyclerView.apply {
            adapter = this@EpisodesFragment.adapter
            this@EpisodesFragment.adapter.setData(episodes)
        }
        binding.episodesListRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) and (page < pages)) {
                    page += 1
                    vm.loadEpisodes(page, filter)
                }
            }
        })
    }

    private fun observeLiveData() {
        vm.episodesLiveData.observe(requireActivity(), Observer(::onEpisodesReceived))
        vm.isErrorLiveData.observe(requireActivity()) {
            if (requireActivity().isFinishing){
                vm.reloadEpisodes(filter)
            } else {
                onErrorReceived()
            }
        }
        vm.isLoadingLiveData.observe(requireActivity(), Observer(::onLoadingStateReceived))
    }

    private fun onEpisodesReceived(episodes: List<Episode>) {
        this.episodes = episodes
        adapter.updateAdapter(episodes)
    }

    private fun onErrorReceived() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.network_connection_no_data)
            .setCancelable(false)
            .setNegativeButton(R.string.network_connection_error_cancel) { dialog, _ -> dialog.cancel() }
            .setPositiveButton(R.string.network_connection_error_action) { _, _ ->
                vm.reloadEpisodes(filter)
            }
            .show()
        filter = EpisodesFilter()
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
        fun newInstance() = EpisodesFragment()
        var page = 1
        var pages = 1
        var filter = EpisodesFilter()
    }
}