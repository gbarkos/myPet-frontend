package com.example.mypet.googlemaps.ui

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.example.mypet.R

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    abstract fun getViewBinding(): VB
    abstract fun setupObservers()
    abstract fun setupViews()

    open fun getStatusBarType(): StatusBarType = StatusBarType.DARK

    open fun getStatusBarColor(): Int? = null

    open fun isFullscreen(): Boolean = false

    open fun isChildFragment(): Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupViews()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!isChildFragment()) updateStatusBarType(getStatusBarType(), getStatusBarColor(), isFullscreen())
        _binding = getViewBinding()
        return _binding?.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun updateStatusBarType(statusBarType: StatusBarType, @ColorRes statusBarColor: Int?, isFullscreen: Boolean) {
        when (statusBarType) {
            StatusBarType.LIGHT -> {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    // prevent light color with white text
                    activity?.window?.statusBarColor = Color.BLACK
                } else {
                    if (isFullscreen) {
                        activity?.window?.statusBarColor = Color.TRANSPARENT
                        activity?.window?.decorView?.systemUiVisibility =
                            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                    } else {
                        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                        context?.let {
                            activity?.window?.statusBarColor = ContextCompat.getColor(it, statusBarColor ?: R.color.white)
                        }
                    }
                }
            }
            StatusBarType.DARK -> {
                activity?.window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
                if (isFullscreen) {
                    activity?.window?.statusBarColor = Color.TRANSPARENT
                } else {
                    context?.let {
                        activity?.window?.statusBarColor = ContextCompat.getColor(it, statusBarColor ?: R.color.black)
                    }
                }
            }
        }
    }

    protected fun restartApp() {
        activity?.apply {
            finish()
            val intent: Intent? = baseContext.packageManager.getLaunchIntentForPackage(baseContext.packageName)
            intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    enum class StatusBarType {
        LIGHT, DARK
    }
}