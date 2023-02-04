package com.example.cleanarchtest.presentation.fragments.splashfragment

import android.content.Context
import android.os.Bundle
import android.view.View
import com.example.cleanarchtest.R
import com.example.cleanarchtest.databinding.SplashScreenFragmentBinding
import com.example.cleanarchtest.presentation.fragments.base.BaseFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

private var listener: ReplaceSplashCallback? = null

class SplashFragment: BaseFragment<SplashScreenFragmentBinding>( R.layout.splash_screen_fragment), CoroutineScope{
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ReplaceSplashCallback) {
            listener = context
        }
    }

    override fun createBinding(view: View): SplashScreenFragmentBinding {
        return SplashScreenFragmentBinding.bind(view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launch {
            delay(2500)
            withContext(Dispatchers.Main) {
                listener?.replaceSplash()
            }
        }
    }
}
