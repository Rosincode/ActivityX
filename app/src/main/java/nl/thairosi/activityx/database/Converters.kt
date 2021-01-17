package nl.thairosi.activityx.database

import android.location.Location
import android.location.LocationManager
import androidx.room.TypeConverter
import java.time.LocalDateTime

/**
 * This class provides the type converters for the Room database
 */
class Converters {

    @TypeConverter
    fun fromLocalDateTime(localDateTime: LocalDateTime?): String {
        return localDateTime.toString()
    }

    @TypeConverter
    fun toLocalDateTime(date: String?): LocalDateTime? {
        return LocalDateTime.parse(date)
    }

    @TypeConverter
    fun fromLocation(location: Location): String {
        return "${location.latitude}, ${location.longitude}"
    }

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