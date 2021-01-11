package nl.thairosi.activityx.ui.home

import androidx.lifecycle.ViewModel
import nl.thairosi.activityx.models.Place
import nl.thairosi.activityx.repository.PlaceRepository

class HomeViewModel(private val placeRepository: PlaceRepository) : ViewModel() {

    fun notFinishedActivity(): Place? {
        return placeRepository.getNotFinishedPlace()
    }

    suspend fun deleteNotFinishedActivity() {
        placeRepository.deleteNotFinishedActivity()
    }

}