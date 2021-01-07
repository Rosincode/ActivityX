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

    @Query("SELECT * FROM visited_places WHERE revealed = 0")
    fun getNotFinishedPlace(): Place?

    @Query("DELETE FROM visited_places WHERE revealed = 0")
    suspend fun deleteNotFinishedPlace()

//    @Query("SELECT *, strftime('%d-%m-%Y', date) as dateSimple FROM visited_places WHERE dateSimple = '04-01-2021' AND dateSimple > '03-01-2021'")
//    fun getVisitedPlaces(): LiveData<List<Place>>

}