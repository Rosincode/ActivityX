package nl.thairosi.activityx.utils

import android.location.LocationManager
import nl.thairosi.activityx.network.PlaceApiModel.Location
import org.junit.Assert.*
import org.junit.Test
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class UtilsUnitTest {

    @Test
    fun typesAdapter_string_contains_only_letters_and_comma() {
        val types = "[bar, casino, movie_theather]"
        val result = Utils.typesAdapter(types)

        assertEquals("bar, casino, movie theather", result)
    }

    @Test
    fun getDateTime_gets_current_date() {
        val dateTimeIso = Utils.getDateTime()
        val result = dateTimeIso.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        val dateFormat = SimpleDateFormat(
            "yyyy-MM-dd", Locale.getDefault())
        val date = Date()
        val expected = dateFormat.format(date)

        assertEquals(expected, result)
    }

}