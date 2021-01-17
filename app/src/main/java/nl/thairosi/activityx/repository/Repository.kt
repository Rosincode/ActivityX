package nl.thairosi.activityx.repository

import androidx.lifecycle.LiveData
import nl.thairosi.activityx.models.Place
import nl.thairosi.activityx.models.PlaceApiModel.PlaceResponse
import nl.thairosi.activityx.models.PlaceApiModel.TextSearchResponse
import retrofit2.Call

interface Repository {
    // API
    fun getPlace(placeId: String): Call<PlaceResponse>
    fun getPlaces(location: String, radius: String, type: String): Call<TextSearchResponse>

    // Database
    suspend fun updateOrInsertPlace(place: Place): Long
    fun getVisitedPlaces(): LiveData<List<Place>>
    fun getUnfinishedPlace(): Place?
    fun getBlockedPlaces(): List<String>?
    suspend fun deleteUnfinishedPlace()
}