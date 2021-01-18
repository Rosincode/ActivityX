package nl.thairosi.activityx.ui.navigation

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

/**
 * This class extends the location LiveData in the navigationViewModel
 *
 * The following permissions are used in this live data class:
 * ACCESS_COARSE_LOCATION
 * ACCESS_FINE_LOCATION
 */
class LocationLiveData(context: Context) : LiveData<Location>() {

    // Properties
    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    // This method removes location updates when the lifecycle owner (Activity) is either paused,
    // stopped or destroyed
    override fun onInactive() {
        super.onInactive()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    // This method executes the startLocationUpdates method when the
    // lifecycle owner (LocationActivity) is either started or resumed
    // MissingPermission is suppressed because we have added permission requests in the HomeFragment
    @SuppressLint("MissingPermission")
    override fun onActive() {
        super.onActive()
        startLocationUpdates()
    }

    // This method starts location updates
    // MissingPermission is suppressed because we have added permission requests in the HomeFragment
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    // Will be invoked when we have a location update from the FusedLocationProviderClient
    // A location update will be set to the value property (inherited from the LiveData object)
    private val locationCallback = object : LocationCallback() {
        // This method maps navigation data to the Location model
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            for (location in locationResult.locations) {
                value = location
            }
        }
    }

    // This companion object sets the interval and accuracy of location updates
    companion object {
        val locationRequest: LocationRequest = LocationRequest.create().apply {
            interval = 250
            fastestInterval = 250
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }
}