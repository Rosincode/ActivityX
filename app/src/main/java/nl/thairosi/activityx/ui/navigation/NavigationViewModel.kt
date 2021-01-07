package nl.thairosi.activityx.ui.navigation

import android.app.Application
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
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

    //LocationLiveData provides a live current GPS location of the phone for this location value
    private val _location = LocationLiveData(application)
    val location: LiveData<Location>
        get() = _location

    private var _place = MutableLiveData(Place())
    val place: LiveData<Place>
        get() = _place

    //OrientationLiveData provides a live current bearing of the phone for this orientation value
    private val _orientation = OrientationLiveData(application)
    val orientation: LiveData<Float>
        get() = _orientation

    fun getRandomPlace(location: String, radius: String, type: String, maxprice: String) {
        val call = PlaceApi.RETROFIT_SERVICE.getPlaces(location = location,
            radius = radius,
            type = type,
            maxprice = maxprice)
        call.enqueue(object : Callback<NearbySearchResponse> {
            override fun onResponse(
                call: Call<NearbySearchResponse>,
                response: Response<NearbySearchResponse>,
            ) {
                if (response.body()?.status.equals("OK")) {
                    val resultList: List<Result> = response.body()?.results?.toList()!!
                    val result = resultList.random()

                    _place.value = Place(
                        placeId = result.place_id,
                        location = locationConverter(
                            result.geometry.location.lat,
                            result.geometry.location.lng
                        ),
                        name = result.name
                    )

                } else {
                    //TODO (report place not found and return to home)
                }
            }

            override fun onFailure(call: Call<NearbySearchResponse>, t: Throwable) {
                //TODO (report failure)
            }
        })
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
