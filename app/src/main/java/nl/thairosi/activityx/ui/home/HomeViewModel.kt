package nl.thairosi.activityx.ui.home

import androidx.lifecycle.ViewModel
import nl.thairosi.activityx.models.Place
import nl.thairosi.activityx.repository.PlaceRepository

/**
 * This viewModel contains the methods for accessing the placeRepository
 */
class HomeViewModel(private val placeRepository: PlaceRepository) : ViewModel() {

    fun getUnfinishedPlace(): Place? {
        return placeRepository.getUnfinishedPlace()
    }

    suspend fun deleteUnfinishedPlace() {
        placeRepository.deleteUnfinishedPlace()
    }

    // Will only be used for demo purposes, it will import five places into the database.
    suspend fun importDemoTestData(place: Place) {
        placeRepository.updateOrInsertPlace(place)
    }

}