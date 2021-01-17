package nl.thairosi.activityx.database

import android.location.LocationManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device
 */
@RunWith(AndroidJUnit4::class)
class ConvertersAndroidUnitTest {
    lateinit var converter: Converters

    @Before
    fun setUp() {
        converter = Converters()
    }

    @Test
    fun tests_if_android_location_converts_to_a_correct_string() {
        val androidLocation = android.location.Location(LocationManager.GPS_PROVIDER)

        androidLocation.latitude = 37.5544407
        androidLocation.longitude = -122.0541085

        val result = converter.fromLocation(androidLocation)
        val expected = "37.5544407, -122.0541085"

        assertEquals(expected, result)
    }

    @Test
    fun tests_if_API_location_converts_to_a_correct_android_location() {
        val expected = android.location.Location(LocationManager.GPS_PROVIDER)

        expected.latitude = 37.5544407
        expected.longitude = -122.0541085

        val locationString = "37.5544407, -122.0541085"
        val result = converter.toLocation(locationString)

        assertEquals(expected.latitude, result.latitude, 0.0)
    }

}