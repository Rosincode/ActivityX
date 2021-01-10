package nl.thairosi.activityx.utils

import android.location.LocationManager
import nl.thairosi.activityx.network.PlaceApiModel.Location
import okhttp3.internal.Util
import org.junit.Assert.*
import org.junit.Test

class UtilsTest {

    @Test
    fun typesAdapter_string_contains_only_letters_and_comma() {
        val types = "[bar, casino, movie_theather]"
        val result = Utils.typesAdapter(types)

        assertEquals("bar, casino, movie theather", result)
    }

    @Test
    fun locationAdapter_create_location_object() {
        val location = Location(37.5544407, -122.0541085)
        val result = Utils.locationAdapter(location)

        var androidLocation = android.location.Location(LocationManager.GPS_PROVIDER)
        androidLocation.latitude = 37.5544407
        androidLocation.longitude = -122.0541085

        assertEquals(result.latitude, androidLocation.latitude, 0.0)
    }





}