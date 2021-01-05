package nl.thairosi.activityx.models

import android.location.Location
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*


/**
 * This model holds the data for an activity
 */
@Entity(
    tableName = "visited_places"
)
@Parcelize
data class Place(
    @PrimaryKey
    val placeId: String = "",
    val photo: String = "",
    val name: String = "",
    val address: String = "",
    val types: String = "",
    val url: String = "",
    val location: Location? = null,
    val date: LocalDateTime? = null,
    val blocked: Boolean = true
) : Parcelable {

    fun getDateToView() : String {
        if (date != null) {
            return date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
        } else {
            return ""
        }
    }


}