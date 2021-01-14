package nl.thairosi.activityx.repository

import androidx.lifecycle.LiveData
import nl.thairosi.activityx.models.Place
import nl.thairosi.activityx.models.PlaceApiModel.PlaceResponse
import retrofit2.Call

interface Repository {
    // API
    fun getPlace(placeId: String): Call<PlaceResponse>

    // Database
    suspend fun updateOrInsert(place: Place): Long
    fun getVisitedPlaces(): LiveData<List<Place>>
    fun getNotFinishedPlace(): Place?
    fun getBlockedPlaces(): List<String>?

    suspend fun deleteNotFinishedActivity()
}