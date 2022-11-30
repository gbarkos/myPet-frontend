package com.example.mypet.googlemaps.util

interface OnConnectivityChangeObserver {

    fun subscribeOnChanges(callback: OnConnectivityChangeCallback)

    fun unsubscribe()
}