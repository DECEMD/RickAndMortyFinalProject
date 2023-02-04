package com.example.cleanarchtest.presentation.characters

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView.OnQueryTextListener
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.cleanarchtest.R
import com.example.cleanarchtest.app.App
import com.example.cleanarchtest.databinding.FragmentCharacterListBinding
import com.example.cleanarchtest.domain.Interactors.CharacterInteractor
import com.example.cleanarchtest.domain.entities.Character
import com.example.cleanarchtest.domain.entities.CharactersFilter
import com.example.cleanarchtest.onNavigate
import com.example.cleanarchtest.presentation.characters.adapters.CharactersAdapter
import com.example.cleanarchtest.presentation.characters.bottomsheet.FilterFragmentCharacters
import com.example.cleanarchtest.presentation.characters.utils.Constants.BUNDLE_FROM_FILTER_FRAGMENT
import com.example.cleanarchtest.presentation.characters.utils.Constants.DATA_FROM_FILTER_FRAGMENT
import com.example.cleanarchtest.presentation.characters.viewmodels.CharactersViewModel
import com.example.cleanarchtest.presentation.characters.viewmodels.CharactersViewModelFactory
import com.example.cleanarchtest.presentation.fragments.base.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class CharactersFragment :
    BaseFragment<FragmentCharacterListBinding>(R.layout.fragment_character_list) {

    override fun createBinding(view: View): FragmentCharacterListBinding {
        return FragmentCharacterListBinding.bind(view)
    }

    @Inject
    lateinit var vmFactory: CharactersViewModelFactory
    lateinit var vm: CharactersViewModel

    @Inject
    lateinit var charactersInteractor: CharacterInteractor

    private val adapter = CharactersAdapter()
    private var queryTextChangedJob: Job? = null
    private lateinit var searchText: String

    private var characters = emptyList<Character>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as App).appComponent.charactersFragmentComponent.inject(this)
        vm = ViewModelProvider(this, vmFactory)[CharactersViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideKeyboard(binding.viewToOperateKeyboard)
        onNavigate().onBackButton()
        binding.characterListSearchView.setOnQueryTextListener(object : OnQueryTextListener {
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

        binding.characterListFilterButton.setOnClickListener {
            FilterFragmentCharacters.show(requireActivity())
            parentFragmentManager.setFragmentResultListener(DATA_FROM_FILTER_FRAGMENT,
                this) { _, result ->
                run {
                    filter = result.getParcelable(BUNDLE_FROM_FILTER_FRAGMENT)!!
                    vm.reloadCharacters(filter)
                }
            }
        }
        initRecyclerView()
        observeLiveData()
        if (characters.isEmpty()) {
            page = 1
            vm.loadCharacters(page, filter)
        }
        binding.characterListResetFilterButton.setOnClickListener {
            binding.characterListSearchView.setQuery("", true)
            filter = CharactersFilter(null, null,null,null)
            vm.reloadCharacters(filter)
        }
        binding.characterListSwipeToRefreshHolder.setOnRefreshListener {
            binding.characterListSearchView.setQuery("", true)
            filter.name = null
            vm.reloadCharacters(filter)
            binding.characterListSwipeToRefreshHolder.isRefreshing = false
        }
    }

    private fun doSearchViewFilter() {
        filter.name = binding.characterListSearchView.query.toString()
        page = 1
        vm.reloadCharacters(filter)
    }

    private fun initRecyclerView() {
        adapter.onClick = { character ->
            onNavigate().onStartCharacterDetailsFragment(character.id)
        }
        binding.characterListRecyclerView.apply {
            adapter = this@CharactersFragment.adapter
            this@CharactersFragment.adapter.setData(characters)
        }
        binding.characterListRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1) and (page < pages)) {
                    page += 1
                    vm.loadCharacters(page, filter)
                }
            }
        })
    }


    private fun observeLiveData() {
        vm.charactersLiveData.observe(requireActivity(), Observer(::onCharactersReceived))
        vm.isErrorLiveData.observe(requireActivity()) {
            if (requireActivity().isFinishing) {
                vm.reloadCharacters(filter)
            } else {
                onErrorReceived()
            }
        }
        vm.isLoadingLiveData.observe(requireActivity(), Observer(::onLoadingStateReceived))
    }

    private fun onCharactersReceived(characters: List<Character>) {
        this.characters = characters
        adapter.updateAdapter(characters)
    }

    private fun onErrorReceived() {
        AlertDialog.Builder(requireContext()).setTitle(R.string.network_connection_no_data)
            .setCancelable(false)
            .setNegativeButton(R.string.network_connection_error_cancel) { dialog, _ -> dialog.cancel() }
            .setPositiveButton(R.string.network_connection_error_action) { _, _ ->
                vm.reloadCharacters(filter)
            }.show()
        filter = CharactersFilter()
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
        fun newInstance() = CharactersFragment()
        var page = 1
        var pages = 1
        var filter = CharactersFilter()
    }
}

