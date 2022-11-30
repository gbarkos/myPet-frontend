package com.example.mypet.googlemaps.usecases

import com.example.mypet.googlemaps.data.MapSearcherRepository
import javax.inject.Inject

class IsLocationServiceEnabledUseCase @Inject constructor(
    private val repository: MapSearcherRepository
) {
    operator fun invoke(): Boolean {
        return repository.isLocationServiceEnabled()
    }
}