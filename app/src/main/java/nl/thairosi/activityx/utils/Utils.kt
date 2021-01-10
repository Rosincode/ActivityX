package nl.thairosi.activityx.utils

import android.location.Location
import android.location.LocationManager
import android.util.Log
import nl.thairosi.activityx.Keys
import nl.thairosi.activityx.network.PlaceAPIService
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class Utils {

    companion object {


        // Helper method to clean string
        fun typesAdapter(types: String): String {
            return types
                .replace("[", "")
                .replace("]", "")
                .replace("_", " ")
        }

        // Helper method for create Location object
        fun locationAdapter(apiLocation: nl.thairosi.activityx.network.PlaceApiModel.Location): Location {
            var androidLocation = Location(LocationManager.GPS_PROVIDER)
            androidLocation.latitude = apiLocation.lat
            androidLocation.longitude = apiLocation.lng
            return androidLocation
        }

        // Helper function to build Image Url
        fun getImageUrl(photoReference: String): String {
            val baseUrl = PlaceAPIService.BASE_URL
            val request = "photo?"
            val maxwidth = "maxwidth=400"
            val reference = "photoreference=$photoReference"
            val key = "key=${Keys.apiKey()}"
            return "$baseUrl$request$maxwidth&$reference&$key"
        }

        //Returns the current local date and time
        fun getDateTime(): LocalDateTime {
            val dateFormat = SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault());
            val dateFormat2 = SimpleDateFormat(
                "HH:mm:ss", Locale.getDefault());
            val date = Date();
            return LocalDateTime.parse(dateFormat.format(date) + "T" + dateFormat2.format(date))
        }


    }


}