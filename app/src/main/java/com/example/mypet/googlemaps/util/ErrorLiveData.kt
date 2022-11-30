package com.example.mypet.googlemaps.util

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorLiveData @Inject constructor() : SingleLiveEvent<Int>()