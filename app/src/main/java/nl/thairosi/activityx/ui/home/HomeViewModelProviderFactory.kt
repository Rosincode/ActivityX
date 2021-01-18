package nl.thairosi.activityx.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import nl.thairosi.activityx.repository.Repository

/**
 * This class provides the viewModel with the placeRepository
 */
@Suppress("UNCHECKED_CAST")
class HomeViewModelProviderFactory(
    private val placeRepository: Repository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(placeRepository) as T
    }
}