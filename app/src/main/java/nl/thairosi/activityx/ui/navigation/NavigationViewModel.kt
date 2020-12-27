package nl.thairosi.activityx.ui.navigation

import android.app.Application
import kotlinx.coroutines.launch
import android.location.Location
import androidx.lifecycle.*


class NavigationViewModel(application: Application) : AndroidViewModel(application) {

    private val _ownLocation = OwnLocationLiveData(application)
    val ownLocation: OwnLocationLiveData
        get() = _ownLocation

    private val _destinationLocation = MutableLiveData<Location>()
    val destinationLocation: LiveData<Location>
        get() = _destinationLocation

    private val _rotationDegrees = MutableLiveData<Float>()
    val rotationDegrees: LiveData<Float>
        get() = _rotationDegrees

    private val _distance = MutableLiveData<Int>()
    val distance: LiveData<Int>
        get() = _distance

    init {
        startNavigation()
    }

    private fun startNavigation() {
        viewModelScope.launch {
//            _placeLocation.value = setPlaceLocation()
            _rotationDegrees.value = setRotationDegrees()
            _distance.value = setDistance()
        }
    }

    private fun setDestinationLocation() : Location {
        TODO("Not yet implemented")
    }

    private fun setDistance() : Int {
        return 200
        TODO("Not yet implemented")
    }

    private fun setRotationDegrees() : Float {
        return 180F
        TODO("Not yet implemented")
    }

}