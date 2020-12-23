package nl.thairosi.activityx.ui.place


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.thairosi.activityx.network.data.PlaceResponse

import nl.thairosi.activityx.network.placeApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PlaceViewModel : ViewModel() {

    private val _response = MutableLiveData<String>()

    val response: LiveData<String>
        get() = _response

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
//                        _response.value = response.body()!!.toString()
                        if (response.body()?.status.equals("OK")) {
                            _response.value = response.body()?.result?.place_id
//                            _response.value = response.body()?.status
                        } else {
                            _response.value = response.body()?.status + "" + response.body()?.error_message
                        }

                    }

                    override fun onFailure(call: Call<PlaceResponse>, t: Throwable) {
                        _response.value = "Invalid!! ${t.message} ${t}"
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