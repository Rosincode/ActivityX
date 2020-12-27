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
 * This class extends the LiveData for the corresponding model
 */
class OwnLocationLiveData(context: Context) : LiveData<nl.thairosi.activityx.models.Location>() {

    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    // Called when the lifecycle owner(LocationActivity) is either paused, stopped or destroyed
    override fun onInactive() {
        super.onInactive()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    // Called when the lifecycle owner(LocationActivity) is either started or resumed
    @SuppressLint("MissingPermission")
    override fun onActive() {
        super.onActive()
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.also {
                    setLocationData(it)
                }
            }
        startLocationUpdates()
    }

    // Call requestLocationUpdates by passing locationRequest object and locationCallback object
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    // Will be invoked when we have a location update from FusedLocationProviderClient
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            for (location in locationResult.locations) {
                setLocationData(location)
            }
        }
    }

    // Map location data to LocationModel. Value is a property inherited from LiveData
    private fun setLocationData(location: Location) {
        value = nl.thairosi.activityx.models.Location(
            longitude = location.longitude,
            latitude = location.latitude
        )
    }

    // The interval and accuracy of the location update
    companion object {
        val locationRequest: LocationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }
}
