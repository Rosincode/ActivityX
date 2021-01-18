package nl.thairosi.activityx

import android.content.Context
import androidx.annotation.VisibleForTesting
import nl.thairosi.activityx.database.PlaceDatabase
import nl.thairosi.activityx.repository.PlaceRepository
import nl.thairosi.activityx.repository.Repository

/**
 * This object is a service to create the repository
 */
object ServiceLocator {

    private var database: PlaceDatabase? = null

    @Volatile
    var Repository: Repository? = null
        @VisibleForTesting set

    private val lock = Any()

    fun provideTasksRepository(context: Context): Repository {
        synchronized(this) {
            return Repository ?: createTasksRepository(context)
        }
    }

    private fun createTasksRepository(context: Context): Repository {
        val newRepo = PlaceRepository(database ?: createDataBase(context))
        Repository = newRepo
        return newRepo
    }

    private fun createDataBase(context: Context): PlaceDatabase {
        val result = PlaceDatabase(context)
        database = result
        return result
    }

    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
            // Clear all data to avoid test pollution.
            database?.apply {
                clearAllTables()
                close()
            }
            database = null
            Repository = null
        }
    }

}