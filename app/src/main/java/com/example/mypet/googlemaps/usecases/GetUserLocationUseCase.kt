package com.example.mypet.googlemaps.usecases

import com.example.mypet.googlemaps.data.MapSearcherRepository
import com.example.mypet.googlemaps.model.UserLocationResultType
import javax.inject.Inject

class GetUserLocationUseCase @Inject constructor(
    private val repository: MapSearcherRepository
) {
    suspend operator fun invoke(): UserLocationResultType {
        return try {
            UserLocationResultType.Success(repository.getUserLocation())
        } catch (ex: Exception) {
            handleException(ex)
        }
    }

    private fun handleException(exception: Exception): UserLocationResultType {
        return when (exception) {
            is NullPointerException -> UserLocationResultType.GpsNotEnabled
            else -> UserLocationResultType.Failure
        }
    }
}