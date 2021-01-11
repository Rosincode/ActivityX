package nl.thairosi.activityx.database

import android.location.LocationManager
import androidx.test.ext.junit.runners.AndroidJUnit4

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
class ConvertersAndroidUnitTest {
    lateinit var converter: Converters

    @Before
    fun setUp() {
        converter = Converters()
    }

    @Test
    fun Converters_fromLocation() {
        var androidLocation = android.location.Location(LocationManager.GPS_PROVIDER)
        androidLocation.latitude = 37.5544407
        androidLocation.longitude = -122.0541085
        val result = converter.fromLocation(androidLocation)

        val expected = "37.5544407, -122.0541085"

        assertEquals(expected, result)
    }

    @Test
    fun Converters_toLocation() {
        var expected = android.location.Location(LocationManager.GPS_PROVIDER)
        expected.latitude = 37.5544407
        expected.longitude = -122.0541085

        val locationString = "37.5544407, -122.0541085"
        val result = converter.toLocation(locationString)

        assertEquals(expected.latitude, result.latitude, 0.0)
    }

}