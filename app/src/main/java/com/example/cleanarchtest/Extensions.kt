package com.example.cleanarchtest

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cleanarchtest.presentation.fragments.OnFragmentToStart

fun Fragment.onNavigate(): OnFragmentToStart {
    return requireActivity() as OnFragmentToStart
}

fun Context.makeToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, duration).show()
}

fun Context.dip2px(value: Int): Int = (resources.displayMetrics.density * value).toInt()

fun MenuItem.avoidMultipleClicks() {
    this.isEnabled = false
    val handler = Handler(Looper.getMainLooper())
    handler.postDelayed({
        this.isEnabled = true
    }, 1000)
}
