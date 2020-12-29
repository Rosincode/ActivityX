package nl.thairosi.activityx.ui.navigation

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.location.Location
import androidx.lifecycle.LiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import nl.thairosi.activityx.models.Navigation

/**
 * This class extends the location LiveData in the navigationViewModel
 */
class NavigationLiveData(context: Context, destination: Location?) : LiveData<Navigation>() {

    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private var accelerometer: Int = Sensor.TYPE_ACCELEROMETER
    private var magnetometer: Int = Sensor.TYPE_MAGNETIC_FIELD


    // Called when the lifecycle owner(LocationActivity) is either paused, stopped or destroyed
    override fun onInactive() {
        super.onInactive()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    // Called when the lifecycle owner(LocationActivity) is either started or resumed
    @SuppressLint("MissingPermission")
    override fun onActive() {
        super.onActive()
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
                if (destination != null) {
                    setNavigationData(location, destination)
                }
            }
        }
    }

    // Map navigation data to the Navigation model. Value is a property inherited from LiveData
    private fun setNavigationData(location: Location, destination: Location) {
        val distance = location.distanceTo(destination)
        val rotation = location.bearingTo(destination)
//        Log.i("Navigation", Sensor.TYPE_ORIENTATION.absoluteValue.toString())
        value = Navigation(rotation, distance)
    }

    // The interval and accuracy of the location update
    companion object {
        val locationRequest: LocationRequest = LocationRequest.create().apply {
            interval = 250
            fastestInterval = 250
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }
}
