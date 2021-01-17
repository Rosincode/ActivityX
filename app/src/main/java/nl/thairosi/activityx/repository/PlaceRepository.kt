package nl.thairosi.activityx.repository

import nl.thairosi.activityx.database.PlaceDatabase
import nl.thairosi.activityx.models.Place
import nl.thairosi.activityx.models.PlaceApiModel.PlaceResponse
import nl.thairosi.activityx.models.PlaceApiModel.TextSearchResponse
import nl.thairosi.activityx.network.PlaceApi
import retrofit2.Call

class PlaceRepository(
    private val db: PlaceDatabase,
) : Repository {
    // API
    override fun getPlace(placeId: String): Call<PlaceResponse> {
        return PlaceApi.RETROFIT_SERVICE.getPlace(place_id = placeId)
    }

    override fun getPlaces(location: String, radius: String, type: String): Call<TextSearchResponse> {
        return PlaceApi.RETROFIT_SERVICE.getPlaces(location = location, radius = radius, type = type)
    }


    // Database
    override suspend fun updateOrInsertPlace(place: Place) = db.getPlaceDao().updateOrInsertPlace(place)
    override fun getVisitedPlaces() = db.getPlaceDao().getVisitedPlaces()
    override fun getUnfinishedPlace() = db.getPlaceDao().getUnfinishedPlace()
    override fun getBlockedPlaces() = db.getPlaceDao().getBlockedPlaces()
    override suspend fun deleteUnfinishedPlace() = db.getPlaceDao().deleteUnfinishedPlace()
}
