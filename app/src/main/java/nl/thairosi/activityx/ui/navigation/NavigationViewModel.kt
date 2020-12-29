package nl.thairosi.activityx.ui.navigation

import android.app.Application
import android.location.Location
import android.location.LocationManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import nl.thairosi.activityx.models.Navigation
import nl.thairosi.activityx.models.Place


/**
 * Preparing data for the navigation UI.
 * Responsible of acquiring and keeping the necessary data.
 * Automatically retained during configuration changes.
 *
 *  If the ViewModel needs the Application context, for example to find a system service,
 *  it can extend the AndroidViewModel class and have a constructor that receives the Application in the constructor,
 *  since Application class extends Context.
 */
class NavigationViewModel(application: Application) : AndroidViewModel(application) {

    private val _place: Place = getRandomPlace()

    private val _navigation = NavigationLiveData(application, _place.location)
    val navigation: LiveData<Navigation>
        get() = _navigation

    private fun getRandomPlace(): Place {
        //TODO: Random place finder method and null check

        //TEST LOCATION
        val androidLocation = Location(LocationManager.GPS_PROVIDER)
        androidLocation.latitude = 52.08915712791165
        androidLocation.longitude = 5.12157871534323
        //

        return Place(location = androidLocation) // must be pushed to place fragment when distance < ### meters
    }

}
