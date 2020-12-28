package nl.thairosi.activityx.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import nl.thairosi.activityx.Keys
import nl.thairosi.activityx.models.Place
import nl.thairosi.activityx.network.PlaceApiModel.PlaceResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//private val moshi = Moshi.Builder()
//    .add(KotlinJsonAdapterFactory())
//    .build()
//
//private val retrofit by lazy {
//    Retrofit.Builder()
//        .baseUrl("https://maps.googleapis.com/maps/api/place/")
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//}

/**
 *SEARCH: https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=1500&type=restaurant&fields=place_id&key=AIzaSyCofXZEndG5WokT6i6n5fdMabW3IWkGiRc
 * PLACE: https://maps.googleapis.com/maps/api/place/details/json?place_id=ChIJN1t_tDeuEmsRUsoyG83frY4&fields=url,types,place_id,opening_hours,geometry,name,formatted_address,photos&key=AIzaSyCofXZEndG5WokT6i6n5fdMabW3IWkGiRc
 * PHOTO: https://maps.googleapis.com/maps/api/place/photo?key=AIzaSyCofXZEndG5WokT6i6n5fdMabW3IWkGiRc&maxwidth=400&photoreference=PHOTO_REFERENCE
 * ATtYBwI1YTZy72VQzYU4I8HgBQC9Ht46iy6FTtrwq4mhLWv_zbfqr9fv3Rlvygc5raPHQCaE7ee
 */

interface PlaceAPIService {

    companion object {
        const val BASE_URL = "https://maps.googleapis.com/maps/api/place/"
        val API_KEY = Keys.apiKey()
    }

    @GET("details/json?")
    fun getPlace(
        @Query("key") key: String = Keys.apiKey(),
        @Query("place_id") place_id: String
    ): Call<PlaceResponse>

}

object PlaceApi {
    val RETROFIT_SERVICE: PlaceAPIService by lazy {
        retrofit.create(PlaceAPIService::class.java)
    }
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit by lazy {
    Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com/maps/api/place/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}