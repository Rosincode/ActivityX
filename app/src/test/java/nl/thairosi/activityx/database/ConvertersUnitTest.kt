package nl.thairosi.activityx.database

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import java.time.LocalDateTime

class ConvertersTest {
    private lateinit var converter: Converters

    @Before
    fun setUp() {
        converter = Converters()
    }

    @Test
    fun fromLocalDateTime_to_String() {
        val localDateTime = LocalDateTime.parse("2021-01-11" + "T" + "01:01:01" )
        val result = converter.fromLocalDateTime(localDateTime)
        val expected = "2021-01-11T01:01:01"
        assertEquals(expected, result)
    }

    @Test
    fun from_String_toLocalDateTime() {
        val stringDate = "2021-01-11T01:01:01"
        val result = converter.toLocalDateTime(stringDate)
        val expected = LocalDateTime.parse("2021-01-11" + "T" + "01:01:01" )
        assertEquals(expected, result)
    }
}