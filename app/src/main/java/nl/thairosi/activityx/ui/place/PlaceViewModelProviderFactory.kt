package nl.thairosi.activityx.ui.place

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import nl.thairosi.activityx.repository.PlaceRepository
import nl.thairosi.activityx.repository.Repository

class PlaceViewModelProviderFactory(
    val placeRepository: Repository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PlaceViewModel(placeRepository) as T
    }
}