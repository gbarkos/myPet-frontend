package com.example.mypet.googlemaps.ui

import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import com.example.mypet.R
import com.example.mypet.databinding.ActivityAppBinding
import com.example.mypet.databinding.ActivityMainBinding.inflate
import com.example.mypet.googlemaps.util.*

@AndroidEntryPoint
class AppActivity : BaseActivity<ActivityAppBinding>() {

    private val viewModel: AppViewModel by viewModels()

    override fun getViewBinding(): ActivityAppBinding = ActivityAppBinding.inflate(layoutInflater)

    fun checkForLocationServices() {
        if (hasPermissions(FOREGROUND_LOCATION_PERMISSIONS)) {
            viewModel.showLocationOnMap()
            return
        } else {
            handleLocationPermissions()
        }
    }

    fun handleLocationPermissions() {
        /*
        * Permissions for indoor beacon tracking and outdoor location Geofences
        */
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

    /*
    * Permissions for indoor beacon tracking and outdoor location Geofences
    */
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
    }
}