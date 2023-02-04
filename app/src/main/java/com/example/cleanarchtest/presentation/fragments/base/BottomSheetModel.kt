package com.example.cleanarchtest.presentation.fragments.base

import com.google.android.material.bottomsheet.BottomSheetBehavior

data class BottomSheetModel(
    val behaviourState: Int = BottomSheetBehavior.SAVE_PEEK_HEIGHT,
    val peekHeightDp: Int? = null
)
