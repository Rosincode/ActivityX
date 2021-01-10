package nl.thairosi.activityx.repository

import nl.thairosi.activityx.database.PlaceDatabase
import nl.thairosi.activityx.models.Place
import nl.thairosi.activityx.network.PlaceApi
import nl.thairosi.activityx.network.PlaceApiModel.PlaceResponse
import retrofit2.Call

class PlaceRepository(
    val db: PlaceDatabase
) {
    // API
    fun getPlace(placeId: String) : Call<PlaceResponse> {
        return PlaceApi.RETROFIT_SERVICE.getPlace(place_id = placeId)
    }

    // Database
    suspend fun updateOrInsert(place: Place) = db.getPlaceDao().updateOrInsert(place)
    fun getVisitedPlaces() = db.getPlaceDao().getVisitedPlaces()
    fun getNotFinishedPlace() = db.getPlaceDao().getNotFinishedPlace()
    suspend fun deleteNotFinishedActivity() = db.getPlaceDao().deleteNotFinishedPlace()

}
