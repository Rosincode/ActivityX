package nl.thairosi.activityx.repository

import nl.thairosi.activityx.models.Place
import nl.thairosi.activityx.network.PlaceApi
import nl.thairosi.activityx.network.PlaceApiModel.PlaceResponse
import retrofit2.Call

class PlaceRepository {

    suspend fun getPlace(place: Place) : Call<PlaceResponse> {
        return PlaceApi.RETROFIT_SERVICE.getPlace(place_id = place.placeId)
    }




}
