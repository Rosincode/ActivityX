package nl.thairosi.activityx.ui.visitedPlaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.thairosi.activityx.models.Place
import nl.thairosi.activityx.repository.Repository

/**
 * This ViewModel prepares data for the VisitedPlaces Fragment,
 * is is responsible of acquiring and keeping the necessary data
 * and is automatically retained during configuration changes
 */
class VisitedPlacesViewModel(private val placeRepository: Repository) : ViewModel() {

    // Properties
    private var _visitedPlaces = MutableLiveData<List<Place>>()
    val visitedPlaces: LiveData<List<Place>>
        get() = _visitedPlaces

    // This method executes the updateOrInsertPlace method in the placeRepository
    suspend fun updateOrInsertPlaceIntoDB(place: Place) = viewModelScope.launch {
        placeRepository.updateOrInsertPlace(place)
    }

    // This method executes the getVisitedPlaces method in the placeRepository
    fun getVisitedPlacesFromDB() = placeRepository.getVisitedPlaces()

}