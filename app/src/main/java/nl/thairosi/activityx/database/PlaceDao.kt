package nl.thairosi.activityx.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import nl.thairosi.activityx.models.Place

/**
 * This interface holds all necessary queries for CRUD database operations
 */
@Dao
interface PlaceDao {

    // Query to update or insert a Place into the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateOrInsertPlace(place: Place): Long

    // Query to get all finished visited places from the database
    @Query("SELECT *, strftime('%d-%m-%Y', date) as dateSimple FROM visited_places WHERE dateSimple IS NOT '01-01-2001' ORDER BY dateSimple desc")
    fun getVisitedPlaces(): LiveData<List<Place>>

    // Query to get all blocked places from the database
    @Query("SELECT placeId FROM visited_places WHERE blocked = 1")
    fun getBlockedPlaces(): List<String>?

    // Query to get the unfinished activity from the database
    @Query("SELECT * FROM visited_places WHERE revealed = 0")
    fun getUnfinishedPlace(): Place?

    // Query to delete the unfinished query from the database
    @Query("DELETE FROM visited_places WHERE revealed = 0")
    suspend fun deleteUnfinishedPlace()
}