package nl.thairosi.activityx.ui.place

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.thairosi.activityx.models.Place
import nl.thairosi.activityx.models.PlaceApiModel.PlaceResponse
import nl.thairosi.activityx.repository.PlaceRepository
import nl.thairosi.activityx.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaceViewModel(
    private val placeRepository: PlaceRepository,
) : ViewModel() {

    private var _place = MutableLiveData<Place>()
    val place: LiveData<Place>
        get() = _place

    // Get Place from Maps API
    fun getPlace(place: Place) {
        viewModelScope.launch {
            val call = placeRepository.getPlace(place.placeId)
            call.enqueue(object : Callback<PlaceResponse> {
                override fun onResponse(
                    call: Call<PlaceResponse>,
                    response: Response<PlaceResponse>,
                ) {
                    if (response.body()?.status.equals("OK")) {
                        place.photoReference =
                            response.body()?.result?.photos?.first()?.photo_reference.toString()
                        place.name = response.body()?.result?.name.toString()
                        place.address = response.body()?.result?.formatted_address.toString()
                        place.types = Utils.typesAdapter(response.body()?.result?.types.toString())
                        place.url = response.body()?.result?.url.toString()
                        place.location = response.body()?.result?.geometry?.location?.let {
                            Utils.locationAdapter(it)
                        }
                        // Update live data
                        _place.value = place
                        // Insert into database
                        updateOrInsert(place)
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

    // Update Place in database
    fun updateOrInsert(place: Place) {
        viewModelScope.launch {
            placeRepository.updateOrInsert(place)
        }
    }
}