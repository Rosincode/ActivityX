package nl.thairosi.activityx.ui.place

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import nl.thairosi.activityx.repository.PlaceRepository
import nl.thairosi.activityx.repository.Repository

/**
 * The PlaceViewModelProviderFactory provides the viewModel with the placeRepository
 */
@Suppress("UNCHECKED_CAST")
class PlaceViewModelProviderFactory(
    val placeRepository: Repository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PlaceViewModel(placeRepository) as T
    }
}