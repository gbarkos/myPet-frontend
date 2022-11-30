package com.example.mypet.googlemaps.util

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.provider.Settings
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.mypet.R
import com.example.mypet.databinding.LayoutGeneralTwoOptionDialogBinding

val Resources.screenHeight: Int
    get() = displayMetrics.heightPixels

val Resources.screenWidth: Int
    get() = displayMetrics.widthPixels

fun Resources.getStatusBarHeight(): Int {
    var statusBarHeight = 0
    val resourceId = getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        statusBarHeight = getDimensionPixelSize(resourceId)
    }
    return statusBarHeight
}

fun Activity.openAppPermissions() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    startActivity(intent)
}

fun Activity.showAlertDialog(
    @StringRes stringRes: Int? = null,
    stringValue: String? = null,
    @StringRes posText: Int = R.string.dialog_action_ok,
    @ColorRes posTextColor: Int = R.color.black,
    @StringRes negText: Int? = null,
    @ColorRes negTextColor: Int = R.color.black,
    onPositive: () -> Unit = {},
    onNegative: () -> Unit = {}
) {
    val alertDialog = AlertDialog.Builder(this)
        .setCancelable(false)
        .create()

    with(LayoutGeneralTwoOptionDialogBinding.inflate(layoutInflater)) {
        alertDialog.setView(root)

        dialogTitle.text = when {
            stringRes != null -> getString(stringRes)
            stringValue != null -> stringValue
            else -> ""
        }
        dialogPosButton.apply {
            text = getString(posText)
            setTextColor(ContextCompat.getColor(this@showAlertDialog, posTextColor))
            setOnClickListener {
                onPositive()
                alertDialog.dismiss()
            }
        }

        negText?.let {
            dialogOptionsSeparator.isVisible = true
            dialogNegButton.apply {
                isVisible = true
                text = getString(negText)
                setTextColor(ContextCompat.getColor(this@showAlertDialog, negTextColor))
                setOnClickListener {
                    onNegative()
                    alertDialog.dismiss()
                }
            }
        }
    }
    alertDialog.show()
}