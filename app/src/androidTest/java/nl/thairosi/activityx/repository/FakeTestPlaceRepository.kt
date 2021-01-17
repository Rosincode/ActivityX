package nl.thairosi.activityx.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.runBlocking
import nl.thairosi.activityx.models.Place
import nl.thairosi.activityx.models.PlaceApiModel.PlaceResponse
import nl.thairosi.activityx.models.PlaceApiModel.TextSearchResponse
import nl.thairosi.activityx.network.PlaceApi
import retrofit2.Call

class FakeTestPlaceRepository : Repository {

    var tasksServiceData: LinkedHashMap<String, Place> = LinkedHashMap()

    private val observableTasks = MutableLiveData<List<Place>>()

    fun addVisitedPlaces(vararg places: Place) {
        for(place in places) {
            tasksServiceData[place.placeId] = place
        }
        observableTasks.value = tasksServiceData.values.toList()
    }

    override fun getPlace(placeId: String): Call<PlaceResponse> {
        return PlaceApi.RETROFIT_SERVICE.getPlace(place_id = placeId)
    }

    override fun getPlaces(
        location: String,
        radius: String,
        type: String,
    ): Call<TextSearchResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun updateOrInsertPlace(place: Place): Long {
        runBlocking {
            tasksServiceData[place.placeId] = place
            observableTasks.postValue(tasksServiceData.values.toList())
        }
        return 1
    }

    override fun getVisitedPlaces(): LiveData<List<Place>> {
        return observableTasks
    }

    override fun getUnfinishedPlace(): Place? {
        TODO("Not yet implemented")
    }

    override fun getBlockedPlaces(): List<String>? {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUnfinishedPlace() {
        TODO("Not yet implemented")
    }

}