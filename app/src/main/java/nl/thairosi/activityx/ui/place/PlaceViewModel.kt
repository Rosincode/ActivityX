package nl.thairosi.activityx.ui.place


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.thairosi.activityx.network.PlaceApi
import nl.thairosi.activityx.network.PlaceApiModel.PlaceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PlaceViewModel : ViewModel() {

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
                        _photo.value = getPhotoUrl(response.body()?.result?.photos?.first()?.photo_reference.toString())
                        _name.value = response.body()?.result?.name
                        _address.value = response.body()?.result?.formatted_address
                        _types.value = response.body()?.result?.types.toString()
                            .replace("[", "")
                            .replace("]", "")
                            .replace("_", " ")
                        _url.value = response.body()?.result?.url
                    } else {
                        _name.value = response.body()?.status + "" + response.body()?.error_message
                    }
                }
                override fun onFailure(call: Call<PlaceResponse>, t: Throwable) {
                    _name.value = "Invalid!! ${t.message} ${t}"
                }
            })
        }
    }

    fun getPhotoUrl(photoReference: String) : String {
        val baseUrl = "https://maps.googleapis.com/maps/api/place/photo?"
        val maxwidth = "maxwidth=400"
        val reference = "&photoreference=$photoReference"
        val key = "&key=AIzaSyCofXZEndG5WokT6i6n5fdMabW3IWkGiRc"
        return baseUrl + maxwidth + reference + key
//        return baseUrl + maxwidth + "&photoreference=ATtYBwJEaK2ExgCTJAmsSH4Mt8jOkTClbFgsgoxp656L0_cWrrW53usWor-2YjA-wIYyqwM-MdECkB_aszV7R8DNFAMnuFAxU0nB_kLGJB6qUF_KIg0zy0Uo47HvGOrfKK2A8x3p_IapNzrq9bor7j6wvblea5buoym3VoSsftnBRnwPEFRl" + key
//        return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=1000&photoreference=ATtYBwJEaK2ExgCTJAmsSH4Mt8jOkTClbFgsgoxp656L0_cWrrW53usWor-2YjA-wIYyqwM-MdECkB_aszV7R8DNFAMnuFAxU0nB_kLGJB6qUF_KIg0zy0Uo47HvGOrfKK2A8x3p_IapNzrq9bor7j6wvblea5buoym3VoSsftnBRnwPEFRl&key=AIzaSyCofXZEndG5WokT6i6n5fdMabW3IWkGiRc"
    }




//        var call = placeApi.RETROFIT_SERVICE.getPlace()
//        call.enqueue(object: retrofit2.Callback<PlaceResponse> {
//            override fun onResponse(call: Call<PlaceResponse>,response: Response<PlaceResponse>) {
//                response.body()!!.toString()
//            }
//
//            override fun onFailure(call: Call<PlaceResponse>,t: Throwable) {
//                t.message
//            }
//        })




}