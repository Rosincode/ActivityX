package nl.thairosi.activityx.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import nl.thairosi.activityx.models.Place

@Dao
interface PlaceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateOrInsert(place: Place): Long

    @Query("SELECT * FROM visited_places")
    fun getVisitedPlaces(): LiveData<List<Place>>

}