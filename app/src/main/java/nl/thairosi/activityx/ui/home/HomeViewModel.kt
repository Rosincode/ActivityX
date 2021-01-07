package nl.thairosi.activityx.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.thairosi.activityx.models.Place
import nl.thairosi.activityx.repository.PlaceRepository

class HomeViewModel(val placeRepository: PlaceRepository) : ViewModel() {

    fun notFinishedActivity() : Place? {
            return placeRepository.getNotFinishedPlace()
    }

    suspend fun deleteNotFinishedActivity() {
            placeRepository.deleteNotFinishedActivity()
    }

}