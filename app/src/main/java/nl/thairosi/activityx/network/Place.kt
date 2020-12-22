package nl.thairosi.activityx.network



import android.os.Parcel
import android.os.Parcelable

/**
 * Gets Mars real estate property information from the Mars API Retrofit service and updates the
 * [MarsProperty] and [MarsApiStatus] [LiveData]. The Retrofit service returns a coroutine
 * Deferred, which we await to get the result of the transaction.
 * @param filter the [MarsApiFilter] that is sent as part of the web server request
 */

//data class Place(val id: String?): Parcelable {
//    constructor(parcel: Parcel) : this(parcel.readString()) {
//    }
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(id)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<Place> {
//        override fun createFromParcel(parcel: Parcel): Place {
//            return Place(parcel)
//        }
//
//        override fun newArray(size: Int): Array<Place?> {
//            return arrayOfNulls(size)
//        }
//    }
//}
