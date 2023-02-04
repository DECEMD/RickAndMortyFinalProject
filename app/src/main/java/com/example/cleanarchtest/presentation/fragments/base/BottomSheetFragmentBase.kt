package com.example.cleanarchtest.presentation.fragments.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.viewbinding.ViewBinding
import com.example.cleanarchtest.R
import com.example.cleanarchtest.dip2px
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

val designBottomSheetId = com.google.android.material.R.id.design_bottom_sheet

abstract class BottomSheetFragmentBase<VB : ViewBinding>(
    @LayoutRes private val layoutRes: Int
): BottomSheetDialogFragment() {

    private var _binding: VB? = null
    protected val binding get() = requireNotNull(_binding) {
        "Binding isn't init"
    }

    private lateinit var bottomSheetBehaviourInitializer: BottomSheetBehaviourInitializer

    open lateinit var bottomSheetModel: BottomSheetModel

    override fun getTheme() = R.style.BottomSheetFragmentStyle

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bottomSheetBehaviourInitializer = BottomSheetBehaviourInitializer(
            requireContext(),
            bottomSheetModel
        )
        val view = inflater.inflate(layoutRes, container, false)
        _binding = createBinding(view)
        return binding.root
//        return inflater.inflate(R.layout.fragment_filter_characters, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheetLayout = this.dialog?.findViewById(designBottomSheetId) as FrameLayout
        bottomSheetBehaviourInitializer.init(bottomSheetLayout, true)

    }
    inner class BottomSheetBehaviourInitializer(
        private val context: Context,
        private val initialBottomSheetModel: BottomSheetModel
    ) {

        fun init(
            bottomSheetLayout: FrameLayout,
            isNeedMathParentBottomSheet: Boolean
        ) {
            if (isNeedMathParentBottomSheet) {
                bottomSheetLayout.layoutParams.height = FrameLayout.LayoutParams.WRAP_CONTENT
            }
            val behavior = BottomSheetBehavior.from(bottomSheetLayout)
            if (initialBottomSheetModel.peekHeightDp != null) {
                behavior.peekHeight = context.dip2px(initialBottomSheetModel.peekHeightDp)
            } else {
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    abstract fun createBinding(view: View): VB
}