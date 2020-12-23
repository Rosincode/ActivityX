package nl.thairosi.activityx.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import nl.thairosi.activityx.network.data.PlaceResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/place/details/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }




    interface PlaceAPIService {

        @GET("json?")
        fun getPlace(
            @Query("place_id") place_id: String = "ChIJW5MOkVpvxkcRDYqgo2pLGBY",
            @Query("key") key: String = "AIzaSyCofXZEndG5WokT6i6n5fdMabW3IWkGiRc"
        ): Call<PlaceResponse>

    }

object placeApi {
    val RETROFIT_SERVICE: PlaceAPIService by lazy {
        retrofit.create(PlaceAPIService::class.java)
    }
}

