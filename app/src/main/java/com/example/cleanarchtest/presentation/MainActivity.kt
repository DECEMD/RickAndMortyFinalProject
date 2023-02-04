package com.example.cleanarchtest.presentation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.cleanarchtest.R
import com.example.cleanarchtest.avoidMultipleClicks
import com.example.cleanarchtest.databinding.ActivityMainBinding
import com.example.cleanarchtest.presentation.characters.CharacterFragment
import com.example.cleanarchtest.presentation.characters.CharactersFragment
import com.example.cleanarchtest.presentation.episodes.EpisodeFragment
import com.example.cleanarchtest.presentation.episodes.EpisodesFragment
import com.example.cleanarchtest.presentation.fragments.OnFragmentToStart
import com.example.cleanarchtest.presentation.fragments.splashfragment.ReplaceSplashCallback
import com.example.cleanarchtest.presentation.fragments.splashfragment.SplashFragment
import com.example.cleanarchtest.presentation.locations.LocationFragment
import com.example.cleanarchtest.presentation.locations.LocationsFragment


class MainActivity: AppCompatActivity(), ReplaceSplashCallback, OnFragmentToStart {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        if (savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, SplashFragment())
                .commit()
        } else {
            showBottomNavPanel()
        }
        initBottomNavigationView()
    }

    override fun replaceSplash() {
        supportFragmentManager.beginTransaction().apply {
            this.setCustomAnimations(R.animator.enter_anim, R.animator.exit_anim)
            this.replace(R.id.fragment_container, CharactersFragment()).commit()
            showBottomNavPanel()
        }
    }

    private fun initBottomNavigationView() {
        binding.bottomNavView.setOnItemSelectedListener { item ->
            item.avoidMultipleClicks()
            when (item.itemId) {
                R.id.characters_fragment -> {
                    onStartCharacterListFragment()
                    true
                }
                R.id.locations_fragment -> {
                    onStartLocationListFragment()
                    true
                }
                R.id.episodes_fragment -> {
                    onStartEpisodeListFragment()
                    true
                }
                else -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, CharactersFragment())
                        .commitAllowingStateLoss()
                    true
                }
            }
        }
    }

    private fun showBottomNavPanel(){
        val slideUp = AnimationUtils.loadAnimation(this@MainActivity, R.anim.slide_up)
        if (binding.bottomNavView.visibility == View.GONE) {
            binding.bottomNavView.visibility = View.VISIBLE
            binding.bottomNavView.startAnimation(slideUp)
        }
    }

    override fun onStartCharacterListFragment() {
        if (supportFragmentManager.backStackEntryCount - 1  < 1) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CharactersFragment.newInstance())
                .commitAllowingStateLoss()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CharactersFragment.newInstance())
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }
    }

    override fun onStartLocationListFragment() {
        if (supportFragmentManager.backStackEntryCount - 1  < 1) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LocationsFragment.newInstance())
                .commitAllowingStateLoss()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LocationsFragment.newInstance())
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }
    }


    override fun onStartEpisodeListFragment() {
        if (supportFragmentManager.backStackEntryCount - 1  < 1) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, EpisodesFragment.newInstance())
                .commitAllowingStateLoss()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, EpisodesFragment.newInstance())
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }
    }

    override fun onStartCharacterDetailsFragment(characterId: Int) {
        slideToStartFloatingButton()
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragment_container,
                CharacterFragment.newInstance(characterId)
            )
            .addToBackStack(null)
            .commit()
    }

    override fun onStartEpisodeDetailsFragment(episodeId: Int) {
        slideToStartFloatingButton()
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container,
                EpisodeFragment.newInstance(episodeId)
            )
            .addToBackStack(null)
            .commit()
    }

    override fun onStartLocationDetailsFragment(locationId: Int) {
        slideToStartFloatingButton()
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container,
                LocationFragment.newInstance(locationId)
            )
            .addToBackStack(null)
            .commit()
    }
    private fun slideToStartFloatingButton(){
        if(binding.floatingButtonBackToNavigate.visibility != View.VISIBLE) {
            val slideStart = AnimationUtils.loadAnimation(this@MainActivity, R.anim.slide_start)
            binding.floatingButtonBackToNavigate.apply{
                visibility = View.VISIBLE
                startAnimation(slideStart)
            }
        }
    }

    override fun onBackButton() {
        binding.floatingButtonBackToNavigate.setOnClickListener {
            if (supportFragmentManager.backStackEntryCount - 1  < 1){
                val slideEnd = AnimationUtils.loadAnimation(this@MainActivity, R.anim.slide_end)
                binding.floatingButtonBackToNavigate.apply{
                    this.isClickable = false
                    val handler = Handler(Looper.getMainLooper())
                    visibility = View.GONE
                    startAnimation(slideEnd)
                    handler.postDelayed({
                        this.isClickable = true
                    }, 2000)

                }
                supportFragmentManager.popBackStack()
            } else {
                supportFragmentManager.popBackStack()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        onBackButton()
    }
}