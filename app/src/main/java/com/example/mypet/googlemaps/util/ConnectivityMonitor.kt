package com.example.mypet.googlemaps.util

import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build


sealed class ConnectivityMonitor : OnConnectivityChangeObserver

@TargetApi(Build.VERSION_CODES.N)
class NougatConnectivityMonitor(
    private val connectivityManager: ConnectivityManager
) : ConnectivityMonitor() {

    private var connectivityCallback: OnConnectivityChangeCallback? = null

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            connectivityCallback?.onConnected()
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            connectivityCallback?.onDisconnected()
        }
    }

    override fun subscribeOnChanges(callback: OnConnectivityChangeCallback) {
        connectivityCallback = callback
        connectivityCallback?.onDisconnected()
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    override fun unsubscribe() {
        connectivityCallback = null
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}

@Suppress("Deprecation")
class LegacyConnectivityMonitor(
    private val context: Context,
    private val connectivityManager: ConnectivityManager
) : ConnectivityMonitor() {

    private var connectivityCallback: OnConnectivityChangeCallback? = null

    private val filter by lazy {
        IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
    }

    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val isConnected = connectivityManager.activeNetworkInfo?.isConnected == true
            if (isConnected) {
                connectivityCallback?.onConnected()
            } else {
                connectivityCallback?.onDisconnected()
            }
        }
    }

    override fun subscribeOnChanges(callback: OnConnectivityChangeCallback) {
        connectivityCallback = callback
        connectivityCallback?.onDisconnected()
        context.registerReceiver(receiver, filter)
    }

    override fun unsubscribe() {
        connectivityCallback = null
        context.unregisterReceiver(receiver)
    }
}
