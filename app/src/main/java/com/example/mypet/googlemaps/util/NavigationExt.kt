package com.example.mypet.googlemaps.util

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

/*
 * prevent open 2 times
 */
fun NavController.singleNavigate(destinationResId: Int, args: Bundle? = null, navOptions: NavOptions? = null) {
    if (currentDestination?.id != destinationResId) {
        navigate(destinationResId, args, navOptions)
    }
}

/*
 * prevent quick-double-tap crash
 */
fun NavController.safeNavigate(action: NavDirections, currentId: Int) {
    if (currentDestination?.id == currentId) {
        navigate(action)
    }
}

fun NavController.safeNavigate(action: NavDirections, extras: Navigator.Extras, currentId: Int) {
    if (currentDestination?.id == currentId) {
        navigate(action, extras)
    }
}

/*
 * pop from back stack otherwise single navigate
 */
fun NavController.checkBackStackNavigate(
    destinationResId: Int,
    bundle: Bundle? = null,
    navOptions: NavOptions = defaultNavOptions(destinationResId, true)
) {
    if ((currentDestination?.id != destinationResId && !popBackStack(destinationResId, false))) {
        navigate(destinationResId, bundle, navOptions)
    }
}

/*
 * navigate deeplink
 */
fun NavController.navigateCustomDeeplink(destinationResId: Int, args: Bundle?) {
    navigate(destinationResId, args, defaultNavOptions(destinationResId, true))
}

/*
 * navigate directly to a specific destination within a nested graph
 */
fun NavController.navigateNestedGraphDeepLink(
    destinationUri: String,
    currentId: Int,
    navOptions: NavOptions? = defaultNavOptions(),
    navigatorExtras: Navigator.Extras? = null
) {
    if (currentDestination?.id == currentId) {
        val uri = Uri.parse(destinationUri)
        navigate(uri, navOptions, navigatorExtras)
    }
}

fun defaultNavOptions(popToId: Int? = null, inclusive: Boolean = false): NavOptions {
    val navOptionsBuilder = NavOptions.Builder()
        .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
        .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
        .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
        .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)

    // check popTo
    popToId?.let {
        navOptionsBuilder.setPopUpTo(it, inclusive)
    }

    return navOptionsBuilder.build()
}