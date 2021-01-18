package nl.thairosi.activityx

import android.app.Application
import nl.thairosi.activityx.repository.Repository

/**
 * This Application provides classes with our singleton repository from the ServiceLocator
 */
class PlaceApplication : Application() {

    val placeRepository: Repository
        get() = ServiceLocator.provideTasksRepository(this)

}