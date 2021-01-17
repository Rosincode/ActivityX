package nl.thairosi.activityx.models

import android.location.Location
import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime

/**
 * This model holds the data for a Place
 *
 * An instantiated Place will be provided with a dummy date to differentiate between
 * finished and unfinished activities
 */
@Entity(
    tableName = "visited_places",
    primaryKeys = ["placeId"]
)

@Parcelize
data class Place(
    var placeId: String = "",
    var photoReference: String = "",
    var name: String = "",
    var address: String = "",
    var types: String = "",
    var url: String = "",
    var location: Location? = null,
    var date: LocalDateTime? = LocalDateTime.parse("2001-01-01T01:01:01"),
    var blocked: Boolean = true,
    var revealed: Boolean = false
) : Parcelable