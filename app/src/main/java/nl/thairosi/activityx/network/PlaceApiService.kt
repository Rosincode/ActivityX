package nl.thairosi.activityx.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//private const val BASE_URL = "https://maps.googleapis.com/maps/api/place/textsearch/xml?query="
//
//enum class PlaceApiFilter(val value: String) {
//    SHOW_BAR("bar")
//}
//
///**
// * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
// * full Kotlin compatibility.
// */
//private val moshi = Moshi.Builder()
//        .add(KotlinJsonAdapterFactory())
//        .build()
//
///**
// * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
// * object.
// */
//private val retrofit = Retrofit.Builder()
//        .addConverterFactory(MoshiConverterFactory.create(moshi))
//        .baseUrl(BASE_URL)
//        .build()

/**
 * A public interface that exposes the [getProperties] method
 */
interface PlaceApiService {
    /**
     * Returns a Coroutine [List] of [MarsProperty] which can be fetched with await() if in a Coroutine scope.
     * The @GET annotation indicates that the "realestate" endpoint will be requested with the GET
     * HTTP method
     */
//    @GET("bar+in+Sydney&key=AIzaSyCofXZEndG5WokT6i6n5fdMabW3IWkGiRc")
//    suspend fun getPlaces(@Query("filter") type: String): List<Place>
}
//
///**
// * A public Api object that exposes the lazy-initialized Retrofit service
// */
//object PlaceApi {
//    val retrofitService : PlaceApiService by lazy { retrofit.create(PlaceApiService::class.java) }
//}
