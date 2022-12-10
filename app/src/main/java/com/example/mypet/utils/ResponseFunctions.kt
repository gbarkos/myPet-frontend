package com.example.mypet.utils

interface ResponseFunctions {

    fun OnStarted()
    fun OnSuccess()
    fun OnFailure(errorMsg: String?)

}