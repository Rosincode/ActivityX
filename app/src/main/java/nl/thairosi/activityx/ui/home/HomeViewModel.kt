package nl.thairosi.activityx.ui.home

import android.location.Location
import android.location.LocationManager
import android.location.LocationManager.GPS_PROVIDER
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
import nl.thairosi.activityx.ui.place.PlaceViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.util.*

class HomeViewModel(
    val placeRepository: PlaceRepository
) : ViewModel() {




}