package nl.thairosi.activityx.database

import android.location.Location
import android.location.LocationManager
import androidx.room.TypeConverter
import java.time.LocalDateTime

/**
 * This global class provides the type converters for the Room database
 */
class Converters {

    // Converts a LocalDateTime to a a String
    @TypeConverter
    fun fromLocalDateTime(localDateTime: LocalDateTime?): String {
        return localDateTime.toString()
    }

    // Converts a String to a LocalDateTime
    @TypeConverter
    fun toLocalDateTime(date: String?): LocalDateTime? {
        return LocalDateTime.parse(date)
    }

    // Converts an Android Location to a String of latitude and longitude
    @TypeConverter
    fun fromLocation(location: Location): String {
        return "${location.latitude}, ${location.longitude}"
    }

    // Converts a String of latitude and longitude to an Android Location
    @TypeConverter
    fun toLocation(location: String): Location {
        val delimiter = ","
        val parts = location.split(delimiter)

        val androidLocation = Location(LocationManager.GPS_PROVIDER)
        androidLocation.latitude = parts[0].toDouble()
        androidLocation.longitude = parts[1].toDouble()

        return androidLocation
    }
}