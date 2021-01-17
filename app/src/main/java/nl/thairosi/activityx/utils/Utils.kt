package nl.thairosi.activityx.utils

import android.location.Location
import android.location.LocationManager
import nl.thairosi.activityx.Keys
import nl.thairosi.activityx.network.PlaceAPIService
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

/**
 * This Utils class contains helper methods for the UI fragments
 */
class Utils {

    companion object {

        // Converts the types to a clean string
        fun typesAdapter(types: String): String {
            return types
                .replace("[", "")
                .replace("]", "")
                .replace("_", " ")
        }

        // Coverts a API Location to a Android Location
        fun apiLocationToAndroidLocation(apiLocation: nl.thairosi.activityx.models.PlaceApiModel.Location): Location {
            val androidLocation = Location(LocationManager.GPS_PROVIDER)
            androidLocation.latitude = apiLocation.lat
            androidLocation.longitude = apiLocation.lng
            return androidLocation
        }

        // Converts latitude & longitude double values to a Android Location
        fun latAndLongToAndroidLocation(lat: Double, lng: Double): Location {
            val androidLocation = Location(LocationManager.GPS_PROVIDER)
            androidLocation.latitude = lat
            androidLocation.longitude = lng
            return androidLocation
        }

        fun latLongToAndroidLocation(location: String): Location {
            val delimiter = ","
            val parts = location.split(delimiter)
            val androidLocation = Location(LocationManager.GPS_PROVIDER)
            androidLocation.latitude = parts[0].toDouble()
            androidLocation.longitude = parts[1].toDouble()
            return androidLocation
        }

        // Builds a Image URL to be used with the Glide library
        fun getImageUrl(photoReference: String): String {
            val baseUrl = PlaceAPIService.BASE_URL
            val request = "photo?"
            val maxwidth = "maxwidth=400"
            val reference = "photoreference=$photoReference"
            val key = "key=${Keys.apiKey()}"
            return "$baseUrl$request$maxwidth&$reference&$key"
        }

        // Returns the current local date and time in ISO format
        fun getDateTime(): LocalDateTime {
            val dateFormat = SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault())
            val dateFormat2 = SimpleDateFormat(
                "HH:mm:ss", Locale.getDefault())
            val date = Date()
            return LocalDateTime.parse(dateFormat.format(date) + "T" + dateFormat2.format(date))
        }

        // Formats a date to be used in the UI
        fun getDateToView(date: LocalDateTime?): String {
            return if (date != null) {
                date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
            } else {
                ""
            }
        }

        fun valueToPercentage(value: Float, total: Float): Float = 100 * (value / total)

        fun percentageToValue(total: Float, percentage: Float): Float = (total / 100) * percentage


    }
}