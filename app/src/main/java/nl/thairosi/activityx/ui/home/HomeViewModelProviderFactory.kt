package nl.thairosi.activityx.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import nl.thairosi.activityx.repository.PlaceRepository

@Suppress("UNCHECKED_CAST")
class HomeViewModelProviderFactory(
    private val placeRepository: PlaceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(placeRepository) as T
    }
}