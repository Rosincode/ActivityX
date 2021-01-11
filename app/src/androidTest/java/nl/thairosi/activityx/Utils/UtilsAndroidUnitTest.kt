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
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class UtilsAndroidUnitTest {

    @Before
    fun setUp() {

    }

        @Test
    fun Utils_getImageUrl_create_correct_image_url_with_key() {
        val imgUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=TEST&key=${Keys.apiKey()}"
        val result = Utils.getImageUrl("TEST")

        assertEquals(imgUrl, result)
    }

    @Test
    fun Utils_locationAdapter_create_android_location_object_from_api_location_object_with_correct_latitude() {
        var location = Location(37.5544407, -122.0541085)
        var result = Utils.locationAdapter(location)

        var androidLocation = android.location.Location(LocationManager.GPS_PROVIDER)
        androidLocation.latitude = 37.5544407
        androidLocation.longitude = -122.0541085
        println(androidLocation.latitude)

        assertEquals(result.latitude, androidLocation.latitude, 0.0)
    }

}