package nl.thairosi.activityx.ui.home

import androidx.lifecycle.ViewModel
import nl.thairosi.activityx.models.Place
import nl.thairosi.activityx.repository.Repository

/**
 * This ViewModel prepares methods for the Home Fragment
 * It contains the methods for accessing the placeRepository
 */
class HomeViewModel(private val placeRepository: Repository) : ViewModel() {

    // This method executes the getUnfinishedPlace method in the placeRepository and returns a Place
    fun getUnfinishedPlaceFromDB(): Place? {
        return placeRepository.getUnfinishedPlace()
    }

    // This method executes the deleteUnfinishedPlace method in the placeRepository
    suspend fun deleteUnfinishedPlaceFromDB() {
        placeRepository.deleteUnfinishedPlace()
    }

    // This method executes the updateOrInsertPlace method in the placeRepository
    // It will only be used for demo purposes, it will import existing places into the database
    suspend fun importDemoTestDataIntoDB(place: Place) {
        placeRepository.updateOrInsertPlace(place)
    }

}