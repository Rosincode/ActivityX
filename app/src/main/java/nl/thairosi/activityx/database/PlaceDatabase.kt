package nl.thairosi.activityx.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import nl.thairosi.activityx.models.Place

/**
 * This abstract class creates a Room database named activityx_db.db
 * The implementation of the abstract function getPlaceDao will be executed for DAO operations
 */
@Database(
    entities = [Place::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class PlaceDatabase : RoomDatabase() {

    abstract fun getPlaceDao(): PlaceDao

    companion object {
        @Volatile
        private var instance: PlaceDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                PlaceDatabase::class.java,
                "activityx_db.db"
            ).build()
    }
}