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

    @Query("SELECT *, strftime('%d-%m-%Y', date) as dateSimple FROM visited_places WHERE dateSimple IS NOT '01-01-2001' ORDER BY dateSimple desc")
    fun getVisitedPlaces(): LiveData<List<Place>>

    @Query("SELECT placeId FROM visited_places WHERE blocked = 1")
    fun getBlockedPlaces(): List<String>?

    @Query("SELECT * FROM visited_places WHERE revealed = 0")
    fun getNotFinishedPlace(): Place?

    @Query("DELETE FROM visited_places WHERE revealed = 0")
    suspend fun deleteNotFinishedPlace()

}