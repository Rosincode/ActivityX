package nl.thairosi.activityx.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * This model holds the data for an activity
 */
@Parcelize
data class Place(
    val placeId: String = "",
    val photo: String = "",
    val name: String = "",
    val address: String = "",
    val types: String = "",
    val url: String = "",
    val date: Date? = null,
    val blocked: Boolean = true
) : Parcelable