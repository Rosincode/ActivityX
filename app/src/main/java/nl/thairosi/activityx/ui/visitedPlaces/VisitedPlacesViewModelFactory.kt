package nl.thairosi.activityx.ui.visitedPlaces

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import nl.thairosi.activityx.repository.PlaceRepository

class VisitedPlacesViewModelFactory(
    val placeRepository: PlaceRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return VisitedPlacesViewModel(placeRepository) as T
    }
}