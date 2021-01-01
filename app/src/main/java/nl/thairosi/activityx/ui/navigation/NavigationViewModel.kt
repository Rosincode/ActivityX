package nl.thairosi.activityx.ui.navigation

import android.app.Application
import android.location.Location
import android.location.LocationManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import nl.thairosi.activityx.models.Place


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

        return Place(location = androidLocation) // must be pushed to place fragment when distance < ### meters
    }

}
