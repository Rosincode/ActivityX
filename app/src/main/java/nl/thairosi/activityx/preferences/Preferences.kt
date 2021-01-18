package nl.thairosi.activityx.preferences

import android.content.SharedPreferences
import android.util.Log

/**
 * This global class contains methods to get the SharedPreferences data
 */
class Preferences { // IC? Annalist? / washup / checkup / intenties / misverstanden

    companion object {

        // Gets the preferred radius from the search criteria and returns a String in meters
        fun getRadius(preferenceManager: SharedPreferences): String {
            val radius = preferenceManager.getInt(
                "criteriaDistanceSeekBar", 2).toString() + "000"
            Log.i("navigation", "Criteria: Radius = $radius meter")
            return radius
        }

        // Gets the preferred types from he search criteria and returns them as a List of Strings
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