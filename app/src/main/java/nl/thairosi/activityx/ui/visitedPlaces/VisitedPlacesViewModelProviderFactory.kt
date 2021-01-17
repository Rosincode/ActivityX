package nl.thairosi.activityx.ui.visitedPlaces

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import nl.thairosi.activityx.repository.PlaceRepository
import nl.thairosi.activityx.repository.Repository

class VisitedPlacesViewModelProviderFactory(
    private val placeRepository: Repository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return VisitedPlacesViewModel(placeRepository) as T
    }
}