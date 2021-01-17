package nl.thairosi.activityx.ui.navigation

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import nl.thairosi.activityx.database.PlaceDatabase
import nl.thairosi.activityx.models.Place
import nl.thairosi.activityx.models.PlaceApiModel.TextSearchResponse
import nl.thairosi.activityx.network.PlaceApi
import nl.thairosi.activityx.repository.PlaceRepository
import nl.thairosi.activityx.utils.Utils
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

    private val placeRepository: PlaceRepository = PlaceRepository(PlaceDatabase(application))

    private var blockedList: List<String> = emptyList()

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

    init {
        getBlockedPlacesFromDB()
    }

    //Gets a random place using the criteria settings and blocked places
    fun getRandomPlaceFromAPI(location: String, radius: String, type: String) {
        val call = PlaceApi.RETROFIT_SERVICE.getPlaces(location = location,
            radius = radius,
            type = type)
        call.enqueue(object : Callback<TextSearchResponse> {
            override fun onResponse(
                call: Call<TextSearchResponse>,
                response: Response<TextSearchResponse>,
            ) {
                if (response.body()?.status.equals("OK")) {
                    val resultList = response.body()?.results?.toMutableList()

                    if (!resultList.isNullOrEmpty()) {
                        if (!resultList.isNullOrEmpty()) {
                            resultList.removeIf {
                                Utils.latLongToAndroidLocation(location).distanceTo(Utils.
                                apiLocationToAndroidLocation(it.geometry.location)) > radius.toInt()
                            }
                            resultList.removeIf {
                                blockedList.contains(it.place_id)
                            }
                        }

                        if (resultList.isNotEmpty()) {
                            val result = resultList.random()
                            _place.value = Place(
                                placeId = result.place_id,
                                location = Utils.latAndLongToAndroidLocation(
                                    result.geometry.location.lat,
                                    result.geometry.location.lng),
                                name = result.name
                            )
                        }
                    }
                }
            }

            override fun onFailure(call: Call<TextSearchResponse>, t: Throwable) {
                Log.i("navigate", "API call failure: $t")
            }
        })
    }

    private fun getBlockedPlacesFromDB() {
        GlobalScope.launch {
            blockedList = placeRepository.getBlockedPlaces()!!
        }
    }

    fun updateOrInsertPlaceIntoDB(place: Place) {
        viewModelScope.launch {
            placeRepository.updateOrInsertPlace(place)
        }
    }

    fun getUnfinishedActivityFromDB(): Place? {
        val notFinishedActivity = placeRepository.getUnfinishedPlace()
        viewModelScope.launch {
            if (notFinishedActivity != null) {
                _place.value = notFinishedActivity
            }
        }
        return notFinishedActivity
    }

}
