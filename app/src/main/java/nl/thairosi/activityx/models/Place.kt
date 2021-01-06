package nl.thairosi.activityx.models

import android.location.Location
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*


/**
 * This model holds the data for an activity
 */
@Entity(
    tableName = "visited_places",
    primaryKeys = ["placeId"]
)
@Parcelize
data class Place(
    var placeId: String = "",
    var photo: String = "",
    var name: String = "",
    var address: String = "",
    var types: String = "",
    var url: String = "",
    var location: Location? = null,
    var date: LocalDateTime? = LocalDateTime.parse("2001-01-01T01:01:01"),
    var blocked: Boolean = true,
    var revealed: Boolean = false
) : Parcelable {
    fun getDateToView() : String {
        return if (date != null) {
            date!!.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
        } else {
            ""
        }
    }
}