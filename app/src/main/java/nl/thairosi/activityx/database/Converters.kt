package nl.thairosi.activityx.database

import android.location.Location
import android.location.LocationManager
import androidx.room.TypeConverter

import java.util.*

class Converters {

    @TypeConverter
    fun toDate(dateLong:Long):Date {
        return Date(dateLong)
    }

    @TypeConverter
    fun fromDate(date: Date):Long{
        return date.time
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

//        val androidLocation = Location(LocationManager.GPS_PROVIDER)
//        androidLocation.latitude = 52.08915712791165
//        androidLocation.longitude = 5.12157871534323
        //

        return androidLocation
    }

}