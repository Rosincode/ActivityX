package nl.thairosi.activityx.database

import android.location.Location
import android.location.LocationManager

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.time.LocalDateTime

import java.util.*


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
    fun toLocation(location: String): android.location.Location {
        val delimiter = ","
        val parts = location.split(delimiter)

        val androidLocation = Location(LocationManager.GPS_PROVIDER)
        androidLocation.latitude = parts[0].toDouble()
        androidLocation.longitude = parts[1].toDouble()

        return androidLocation
    }

}