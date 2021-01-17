package nl.thairosi.activityx.network

import nl.thairosi.activityx.Keys
import nl.thairosi.activityx.models.PlaceApiModel.PlaceResponse
import nl.thairosi.activityx.models.PlaceApiModel.TextSearchResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * The PlaceAPIService contains the BASE_URL and the necessary Queries for our application
 * The service is used to query the Google API services
 */
interface PlaceAPIService {

    companion object {
        const val BASE_URL = "https://maps.googleapis.com/maps/api/place/"
    }

    //Google API call to get one place by its place_id
    @GET("details/json?")
    fun getPlace(
        @Query("key") key: String = Keys.apiKey(),
        @Query("place_id") place_id: String?
    ): Call<PlaceResponse>

    //Google API call to get multiple places of a chosen type within a chosen radius
    @GET("textsearch/json?")
    fun getPlaces(
        @Query("key") key: String = Keys.apiKey(),
        @Query("radius") radius: String,
        @Query("opennow") opennow: String = "true",
        @Query("location") location: String,
        @Query("type") type: String
    ): Call<TextSearchResponse>
}

object PlaceApi {
    val RETROFIT_SERVICE: PlaceAPIService by lazy {
        retrofit.create(PlaceAPIService::class.java)
    }
}

private val retrofit by lazy {
    Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com/maps/api/place/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}