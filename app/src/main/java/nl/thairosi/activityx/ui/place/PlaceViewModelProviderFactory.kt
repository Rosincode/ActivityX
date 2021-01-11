package nl.thairosi.activityx.ui.place

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import nl.thairosi.activityx.repository.PlaceRepository

class PlaceViewModelProviderFactory(
    val placeRepository: PlaceRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PlaceViewModel(placeRepository) as T
    }
}