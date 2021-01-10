package nl.thairosi.activityx.ui.visitedPlaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.thairosi.activityx.models.Place
import nl.thairosi.activityx.repository.PlaceRepository

class VisitedPlacesViewModel(
    val placeRepository: PlaceRepository
    ) : ViewModel() {

    private var _visitedPlaces = MutableLiveData<List<Place>>()
    val visitedPlaces: LiveData<List<Place>>
        get() = _visitedPlaces

    suspend fun updateOrInsert(place: Place) = viewModelScope.launch {
        placeRepository.updateOrInsert(place)
    }

    fun getVisitedPlacesFromDB() = placeRepository.getVisitedPlaces()

}