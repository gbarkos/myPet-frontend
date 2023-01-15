package com.example.mypet.googlemaps.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import com.example.mypet.R
import com.example.mypet.databinding.ActivityAppBinding
import com.example.mypet.googlemaps.util.*

@AndroidEntryPoint
class AppActivity : BaseActivity<ActivityAppBinding>() {

    private val viewModel: AppViewModel by viewModels()

    override fun getViewBinding(): ActivityAppBinding = ActivityAppBinding.inflate(layoutInflater)

    private var navigationId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handleIntent(intent)
        when (navigationId) {
            0 -> {

            }
            else -> navigationId?.let {
                handleNavigationGraph(it)
            }
        }
    }

    private fun handleNavigationGraph(navigationId: Int) {

        val navController = Navigation.findNavController(
            this@AppActivity,
            R.id.nav_host_fragment
        )

        try {
            navController.setGraph(navigationId, Bundle())
        } catch (e: Exception) {
            finish()
        }
    }

    fun checkForLocationServices() {
        if (hasPermissions(FOREGROUND_LOCATION_PERMISSIONS)) {
            viewModel.showLocationOnMap()
            return
        } else {
            handleLocationPermissions()
        }
    }

    fun handleLocationPermissions() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                if (hasPermissions(FOREGROUND_LOCATION_PERMISSIONS + BACKGROUND_LOCATION_PERMISSIONS)) {
                    return
                }
                requestPermissions(FOREGROUND_LOCATION_PERMISSIONS, FOREGROUND_LOCATION_PERMISSIONS_RC)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                if (hasPermissions(FOREGROUND_LOCATION_PERMISSIONS + BACKGROUND_LOCATION_PERMISSIONS)) {
                    return
                }
                requestPermissions(FOREGROUND_LOCATION_PERMISSIONS + BACKGROUND_LOCATION_PERMISSIONS, FOREGROUND_LOCATION_PERMISSIONS_RC)
            }
            else -> {
                if (hasPermissions(FOREGROUND_LOCATION_PERMISSIONS)) {
                    return
                }
                requestPermissions(FOREGROUND_LOCATION_PERMISSIONS, FOREGROUND_LOCATION_PERMISSIONS_RC)
            }
        }
    }

    fun handleIntent(intent: Intent?) {
        intent?.extras?.apply {
            navigationId = getInt(NAVIGATION_GRAPH_ID)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == FOREGROUND_LOCATION_PERMISSIONS_RC) {
            if (grantResults.isNotEmpty()) {
                if (grantResults.any { it != PackageManager.PERMISSION_GRANTED }) {
                    showAlertDialog(
                        stringRes = R.string.error_foreground_location_permissions,
                        posText = R.string.dialog_action_settings,
                        negText = R.string.dialog_action_cancel,
                        negTextColor = R.color.black,
                        onPositive = {
                            openAppPermissions()
                        }
                    )
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        showAlertDialog(
                            stringRes = R.string.error_background_location_permission_message,
                            posText = R.string.dialog_action_settings,
                            negText = R.string.dialog_action_cancel,
                            negTextColor = R.color.black,
                            onPositive = {
                                openAppPermissions()
                            },
                            onNegative = {
                            }
                        )
                        return
                    }
                    viewModel.showLocationOnMap()
                }
            }
        }
    }

    companion object {
        const val FOREGROUND_LOCATION_PERMISSIONS_RC = 1407

        private const val NAVIGATION_GRAPH_ID = "NAVIGATION_GRAPH_ID"

        fun show(activity: Activity, navigationId: Int) {
            val intent = Intent(activity, AppActivity::class.java)

            intent.putExtras(Bundle().apply {
                putInt(NAVIGATION_GRAPH_ID, navigationId)
            })

            activity.startActivityForResult(intent,10)
        }
    }
}