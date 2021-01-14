package nl.thairosi.activityx.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.runBlocking
import nl.thairosi.activityx.models.Place
import nl.thairosi.activityx.models.PlaceApiModel.PlaceResponse
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
        TODO("Not yet implemented")
    }

    override suspend fun updateOrInsert(place: Place): Long {
        runBlocking {
            tasksServiceData[place.placeId] = place
            observableTasks.postValue(tasksServiceData.values.toList())
        }
        return 1
    }

    override fun getVisitedPlaces(): LiveData<List<Place>> {
        return observableTasks
    }

    override fun getNotFinishedPlace(): Place? {
        TODO("Not yet implemented")
    }

    override fun getBlockedPlaces(): List<String>? {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNotFinishedActivity() {
        TODO("Not yet implemented")
    }

}