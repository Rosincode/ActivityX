package nl.thairosi.activityx.Utils

import android.location.LocationManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import nl.thairosi.activityx.Keys
import nl.thairosi.activityx.models.PlaceApiModel.Location
import nl.thairosi.activityx.utils.Utils


import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device
 */
@RunWith(AndroidJUnit4::class)
class UtilsAndroidUnitTest {

    @Test
    fun tests_if_a_correct_img_url_is_returned() {
        val imgUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=TEST&key=${Keys.apiKey()}"
        val result = Utils.getImageUrl("TEST")

        assertEquals(imgUrl, result)
    }

    @Test
    fun tests_if_an_API_location_is_correctly_converted_to_android_location() {
        val location = Location(37.5544407, -122.0541085)
        val result = Utils.locationAdapter(location)

        val androidLocation = android.location.Location(LocationManager.GPS_PROVIDER)
        androidLocation.latitude = 37.5544407
        androidLocation.longitude = -122.0541085

        assertEquals(result.latitude, androidLocation.latitude, 0.0)
    }

}