package nl.thairosi.activityx.preferences

import android.content.SharedPreferences
import android.util.Log

/**
 * This Preferences class contains methods to get SharedPreferences data
 */
class Preferences {

    companion object {

        fun getRadius(preferenceManager: SharedPreferences): String {
            val radius = preferenceManager.getInt(
                "criteriaDistanceSeekBar", 2).toString() + "000"
            Log.i("navigation", "Criteria: Radius = $radius meter")
            return radius
        }

        fun getTypes(preferenceManager: SharedPreferences): List<String> {
            val typesDefault = setOf("night_club", "bar", "bowling_alley", "cafe", "movie_theater",
                "museum", "restaurant", "casino", "park")
            var typesPreferences =
                preferenceManager.getStringSet("multi_select_list_types", typesDefault)
            if (typesPreferences.isNullOrEmpty()) {
                Log.i("navigation", "Criteria: No types set so all types are used")
                typesPreferences = typesDefault
            }
            val shuffledTypes = typesPreferences.shuffled()
            Log.i("navigation", "Criteria: Types = $shuffledTypes")
            return shuffledTypes
        }
    }

}