package nl.thairosi.activityx.repository

import androidx.lifecycle.LiveData
import nl.thairosi.activityx.models.Place
import nl.thairosi.activityx.models.PlaceApiModel.PlaceResponse
import nl.thairosi.activityx.models.PlaceApiModel.TextSearchResponse
import retrofit2.Call

/**
 * This is the interface for our repository class that contain all the abstract repository methods
 */
interface Repository {

    // Abstract API methods
    fun getPlace(placeId: String): Call<PlaceResponse>
    fun getPlaces(location: String, radius: String, type: String): Call<TextSearchResponse>

    // Abstract Database methods
    suspend fun updateOrInsertPlace(place: Place): Long
    fun getVisitedPlaces(): LiveData<List<Place>>
    fun getUnfinishedPlace(): Place?
    fun getBlockedPlaces(): List<String>?
    suspend fun deleteUnfinishedPlace()
}