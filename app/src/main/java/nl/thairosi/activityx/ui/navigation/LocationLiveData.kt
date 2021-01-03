package nl.thairosi.activityx.ui.navigation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.location.Location
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

/**
 * This class extends the location LiveData in the navigationViewModel
 */
class LocationLiveData(context: Context) : LiveData<Location>() {

    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    // Called when the lifecycle owner(Activity) is either paused, stopped or destroyed
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
    // Maps navigation data to the Location model. Value is a property inherited from LiveData
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            for (location in locationResult.locations) {
                value = location
            }
        }
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