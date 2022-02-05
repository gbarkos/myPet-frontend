package com.example.mypet.utils

sealed class NetworkLoadingState {
    object OnLoading : NetworkLoadingState()
    object OnSuccess : NetworkLoadingState()
    data class OnError(val message: String) : NetworkLoadingState()
}
