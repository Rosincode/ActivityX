package nl.thairosi.activityx.utils

import org.junit.Assert.*
import org.junit.Test
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * This test class tests important Utils methods
 */
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

    @Test
    fun value_is_calculated_to_a_correct_percentage() {
        val total = 100F
        val value = 20F
        val result = Utils.valueToPercentage(value, total)
        val expected = 20F
        assertEquals(expected, result)
    }

    @Test
    fun percentage_is_calculated_to_a_correct_value() {
        val total = 100F
        val percentage = 80F
        val result = Utils.percentageToValue(total, percentage)
        val expected = 80F
        assertEquals(expected, result)
    }

}