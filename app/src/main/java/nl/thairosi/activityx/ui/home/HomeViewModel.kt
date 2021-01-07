package nl.thairosi.activityx.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.thairosi.activityx.models.Place
import nl.thairosi.activityx.repository.PlaceRepository

class HomeViewModel(val placeRepository: PlaceRepository) : ViewModel() {

    private var _place = MutableLiveData(Place())
    val place: LiveData<Place>
        get() = _place

    init {
        notFinishedActivity()
    }

    fun notFinishedActivity() {
        viewModelScope.launch {
            _place.value = placeRepository.getNotFinishedPlace()
        }
    }

    fun deleteNotFinishedActivity() {
        viewModelScope.launch {
            placeRepository.deleteNotFinishedActivity()
        }
    }

}