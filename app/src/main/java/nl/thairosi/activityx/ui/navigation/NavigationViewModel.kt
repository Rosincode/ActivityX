package nl.thairosi.activityx.ui.navigation

import android.app.Application
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.thairosi.activityx.models.Place
import nl.thairosi.activityx.network.NearbySearchApiModel.NearbySearchResponse
import nl.thairosi.activityx.network.NearbySearchApiModel.Result
import nl.thairosi.activityx.network.PlaceApi
import nl.thairosi.activityx.network.PlaceApiModel.PlaceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


/**
 * Preparing data for the navigation UI.
 * Responsible of acquiring and keeping the necessary data.
 * Automatically retained during configuration changes.
 * Extends AndroidViewModel and has a application constructor for live data system services
 */
class NavigationViewModel(application: Application) : AndroidViewModel(application) {

    //GetPlace retrieves the randomly picked location to navigate to for this destination value
    private val _destination: Location = getPlace().location!!
    val destination: Location
        get() = _destination

    //LocationLiveData provides a live current GPS location of the phone for this location value
    private val _location = LocationLiveData(application)
    val location: LiveData<Location>
        get() = _location

    //OrientationLiveData provides a live current bearing of the phone for this orientation value
    private val _orientation = OrientationLiveData(application)
    val orientation: LiveData<Float>
        get() = _orientation

    //Retrieves the randomly picked location
    private fun getPlace(): Place {
        //TODO: Random place finder method and null check

        //THIS IS A TEST LOCATION OF KAFE BELGIE IN UTRECHT:
        val androidLocation = Location(LocationManager.GPS_PROVIDER)
        androidLocation.latitude = 52.08915712791165
        androidLocation.longitude = 5.12157871534323
        getRandomPlace("-33.8670522,151.1957362", "1500", "restaurant")
        return Place(location = androidLocation) // must be pushed to place fragment when distance < ### meters
    }

    private fun getRandomPlace(location: String, radius: String, type: String) {
        viewModelScope.launch {
            val call = PlaceApi.RETROFIT_SERVICE.getPlaces(location = location, radius = radius, type = type)
            call.enqueue(object : Callback<NearbySearchResponse> {
                override fun onResponse(
                    call: Call<NearbySearchResponse>,
                    response: Response<NearbySearchResponse>,
                ) {
                    if (response.body()?.status.equals("OK")) {
                        var places: List<Result> = response.body()?.results?.toList()!!
                        for (i in places) {
                            Log.i("Navigation", "$i")
                        }

                    } else {
                        //TODO (report place not found and return to home)
                    }
                }
                override fun onFailure(call: Call<NearbySearchResponse>, t: Throwable) {
                    //TODO (report failure)
                }
            })
        }
    }

}