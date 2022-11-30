package com.example.mypet.utils

interface AuthFunctions {

    fun OnStarted()
    fun OnSuccess()
    fun OnFailure(errorCode: MutableList<Int>?)

}