package nl.thairosi.activityx.ui.visitedPlaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.thairosi.activityx.models.Place
import nl.thairosi.activityx.repository.PlaceRepository
import nl.thairosi.activityx.repository.Repository

class VisitedPlacesViewModel(
    private val placeRepository: Repository
) : ViewModel() {

    private var _visitedPlaces = MutableLiveData<List<Place>>()
    val visitedPlaces: LiveData<List<Place>>
        get() = _visitedPlaces

    suspend fun updateOrInsertPlaceIntoDB(place: Place) = viewModelScope.launch {
        placeRepository.updateOrInsertPlace(place)
    }

    fun getVisitedPlacesFromDB() = placeRepository.getVisitedPlaces()

}