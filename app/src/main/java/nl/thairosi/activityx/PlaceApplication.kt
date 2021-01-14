package nl.thairosi.activityx

import android.app.Application
import nl.thairosi.activityx.repository.Repository

class PlaceApplication : Application() {

    val taskRepository: Repository
        get() = ServiceLocator.provideTasksRepository(this)

}