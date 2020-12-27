package nl.thairosi.activityx.ui.place

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.thairosi.activityx.models.Place

import nl.thairosi.activityx.network.PlaceApi
import nl.thairosi.activityx.network.PlaceApiModel.PlaceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaceViewModel : ViewModel() {

    private var _place = MutableLiveData<Place>()
    val place: LiveData<Place>
        get() = _place

    private val _photo = MutableLiveData<String>()
    val photo: LiveData<String>
        get() = _photo

    private val _name = MutableLiveData<String>()
    val name: LiveData<String>
        get() = _name

    private val _address = MutableLiveData<String>()
    val address: LiveData<String>
        get() = _address

    private val _types = MutableLiveData<String>()
    val types: MutableLiveData<String>
        get() = _types

    private val _url = MutableLiveData<String>()
    val url: LiveData<String>
        get() = _url

    init {
        getPlace()
    }

    private fun getPlace() {
        viewModelScope.launch {
            val call = PlaceApi.RETROFIT_SERVICE.getPlace()
            call.enqueue(object : Callback<PlaceResponse> {
                override fun onResponse(
                    call: Call<PlaceResponse>,
                    response: Response<PlaceResponse>
                ) {
                    if (response.body()?.status.equals("OK")) {
//                        var currentPlace = Place(
//                            photo = photoAdapter(response.body()?.result?.photos?.first()?.photo_reference.toString()),
//                            name = response.body()?.result?.name.toString(),
//                            address = response.body()?.result?.formatted_address.toString(),
//                            types = typesAdapter(response.body()?.result?.types.toString()),
//                            url = response.body()?.result?.url.toString()
//                        )
                            _photo.value = photoAdapter(response.body()?.result?.photos?.first()?.photo_reference.toString())
                            _name.value  = response.body()?.result?.name.toString()
                            _address.value = response.body()?.result?.formatted_address.toString()
                            _types.value  = typesAdapter(response.body()?.result?.types.toString())
                            _url.value  = response.body()?.result?.url.toString()
                    } else {
                        _name.value = response.body()?.status + "" + response.body()?.error_message
                    }
                }
                override fun onFailure(call: Call<PlaceResponse>, t: Throwable) {
                    _name.value = "Invalid!! ${t.message} $t"
                }
            })
        }
    }

    private fun photoAdapter(photoReference: String) : String {
        val baseUrl = "https://maps.googleapis.com/maps/api/place/photo?"
        val maxwidth = "maxwidth=400"
        val reference = "&photoreference=$photoReference"
        val key = "&key=AIzaSyCofXZEndG5WokT6i6n5fdMabW3IWkGiRc"
        return baseUrl + maxwidth + reference + key
    }

    private fun typesAdapter(types: String) : String {
        return types
            .replace("[", "")
            .replace("]", "")
            .replace("_", " ")
    }
}