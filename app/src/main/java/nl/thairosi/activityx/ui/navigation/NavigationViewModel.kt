package nl.thairosi.activityx.ui.navigation

import android.app.Application
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import nl.thairosi.activityx.database.PlaceDatabase
import nl.thairosi.activityx.models.Place
import nl.thairosi.activityx.network.NearbySearchApiModel.NearbySearchResponse
import nl.thairosi.activityx.network.NearbySearchApiModel.Result
import nl.thairosi.activityx.network.PlaceApi
import nl.thairosi.activityx.repository.PlaceRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Preparing data for the navigation UI.
 * Responsible of acquiring and keeping the necessary data.
 * Automatically retained during configuration changes.
 * Extends AndroidViewModel and has a application constructor for live data system services
 */
class NavigationViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PlaceRepository = PlaceRepository(PlaceDatabase(application))

    private var blockedList : List<String> = emptyList()

    //LocationLiveData provides a live current GPS location of the phone for this location value
    private val _location = LocationLiveData(application)
    val location: LiveData<Location>
        get() = _location

    //The place MutableLiveData will be filled with data when a place is found
    private var _place = MutableLiveData(Place())
    val place: LiveData<Place>
        get() = _place

    //OrientationLiveData provides a live current bearing of the phone for this orientation value
    private val _orientation = OrientationLiveData(application)
    val orientation: LiveData<Float>
        get() = _orientation

    //Gets a random place using the criteria settings and blocked places
    fun getRandomPlace(location: String, radius: String, type: String) {
        val call = PlaceApi.RETROFIT_SERVICE.getPlaces(location = location,
            radius = radius,
            type = type)
        call.enqueue(object : Callback<NearbySearchResponse> {
            override fun onResponse(
                call: Call<NearbySearchResponse>,
                response: Response<NearbySearchResponse>,
            ) {
                if (response.body()?.status.equals("OK")) {
                    val resultList: MutableList<Result>? = response.body()?.results?.toMutableList()!!

                    if(!resultList.isNullOrEmpty()) {
                        getBlockedPlaces()
                        if (!blockedList.isNullOrEmpty()) {
                            resultList.forEach {
                                if (blockedList.contains(it.place_id)) {
                                    resultList.removeAt(resultList.indexOf(it))
                                }
                            }
                        }

                        if (resultList.isNotEmpty()) {
                            val result = resultList.random()
                            _place.value = Place(
                                placeId = result.place_id,
                                location = locationConverter(
                                    result.geometry.location.lat,
                                    result.geometry.location.lng),
                                name = result.name
                            )
                        }
                    }
                }
            }
            override fun onFailure(call: Call<NearbySearchResponse>, t: Throwable) {
                Log.i("navigate", "API call failure: $t")
            }
        })
    }

    private fun getBlockedPlaces() {
        GlobalScope.launch {
            blockedList = repository.getBlockedPlaces()!!
        }
    }

    fun addToDatabase(place: Place) {
        viewModelScope.launch {
            repository.updateOrInsert(place)
        }
    }

    fun notFinishedActivity() : Place? {
        val notFinishedActivity = repository.getNotFinishedPlace()
        viewModelScope.launch {
            if (notFinishedActivity != null) {
                _place.value = notFinishedActivity
            }
        }
        return notFinishedActivity
    }

    private fun locationConverter(lat: Double, lng: Double): Location {
        val androidLocation = Location(LocationManager.GPS_PROVIDER)
        androidLocation.latitude = lat
        androidLocation.longitude = lng
        return androidLocation
    }
}
