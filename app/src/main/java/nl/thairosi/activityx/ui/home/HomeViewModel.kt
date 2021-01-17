package nl.thairosi.activityx.ui.home

import androidx.lifecycle.ViewModel
import nl.thairosi.activityx.models.Place
import nl.thairosi.activityx.repository.PlaceRepository

/**
 * This viewModel contains the methods for accessing the placeRepository
 */
class HomeViewModel(private val placeRepository: PlaceRepository) : ViewModel() {

    fun getUnfinishedPlaceFromDB(): Place? {
        return placeRepository.getUnfinishedPlace()
    }

    suspend fun deleteUnfinishedPlaceFromDB() {
        placeRepository.deleteUnfinishedPlace()
    }

    // Will only be used for demo purposes, it will import existing places into the database.
    suspend fun importDemoTestDataIntoDB(place: Place) {
        placeRepository.updateOrInsertPlace(place)
    }

}