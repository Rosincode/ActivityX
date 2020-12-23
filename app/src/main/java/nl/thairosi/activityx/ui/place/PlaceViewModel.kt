package nl.thairosi.activityx.ui.place


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.thairosi.activityx.network.PlaceApiModel.Photo
import nl.thairosi.activityx.network.PlaceApiModel.PlaceResponse

import nl.thairosi.activityx.network.placeApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL


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

                val call = placeApi.RETROFIT_SERVICE.getPlace()
                call.enqueue(object : Callback<PlaceResponse> {
                    override fun onResponse(
                        call: Call<PlaceResponse>,
                        response: Response<PlaceResponse>
                    ) {
                        if (response.body()?.status.equals("OK")) {
                            _photo.value = response.body()?.result?.photos?.get(0).toString()
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