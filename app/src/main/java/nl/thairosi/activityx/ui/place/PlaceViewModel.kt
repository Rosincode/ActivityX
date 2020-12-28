package nl.thairosi.activityx.ui.place

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.thairosi.activityx.Keys
import nl.thairosi.activityx.models.Place
import nl.thairosi.activityx.network.PlaceApi
import nl.thairosi.activityx.network.PlaceApiModel.PlaceResponse
import nl.thairosi.activityx.repository.PlaceRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaceViewModel(
    val placeRepository: PlaceRepository
) : ViewModel() {

    private var _place = MutableLiveData<Place>()
    val place: LiveData<Place>
        get() = _place

    init {
        getPlace("ChIJW5MOkVpvxkcRDYqgo2pLGBY")
    }

    private fun getPlace(placeId: String) {
        viewModelScope.launch {
            val call = PlaceApi.RETROFIT_SERVICE.getPlace(place_id = placeId)
            call.enqueue(object : Callback<PlaceResponse> {
                override fun onResponse(
                    call: Call<PlaceResponse>,
                    response: Response<PlaceResponse>,
                ) {
                    if (response.body()?.status.equals("OK")) {
                        _place.value = Place(
                            placeId = response.body()?.result?.place_id.toString(),
                            photo = photoAdapter(response.body()?.result?.photos?.first()?.photo_reference.toString()),
                            name = response.body()?.result?.name.toString(),
                            address = response.body()?.result?.formatted_address.toString(),
                            types = typesAdapter(response.body()?.result?.types.toString()),
                            url = response.body()?.result?.url.toString()
                        )
                    } else {
                        _place.value = Place(name = "Place not found")
                    }
                }

                override fun onFailure(call: Call<PlaceResponse>, t: Throwable) {
                    _place.value =
                        Place(name = "No response from the API service:\n\n${t.message}\n\n$t")
                }
            })
        }
    }

    private fun photoAdapter(photoReference: String): String {
        val base = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
        return base + photoReference + "&key=" + Keys.apiKey()
    }

    private fun typesAdapter(types: String): String {
        return types
            .replace("[", "")
            .replace("]", "")
            .replace("_", " ")
    }
}