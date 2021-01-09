package nl.thairosi.activityx.ui.navigation

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.updatePaddingRelative
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.fragment_navigation.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import nl.thairosi.activityx.R
import nl.thairosi.activityx.databinding.FragmentNavigationBinding
import nl.thairosi.activityx.models.Place
import java.text.SimpleDateFormat
import java.util.*
import java.time.LocalDateTime as LocalDateTime

class NavigationFragment : Fragment() {
    //Properties
    private lateinit var location: Location
    private var place: Place = Place()
    private var orientation: Float = 0F
    private var distance: Float = 10000F
    private var radius: String = "2000"
    private var types : List<String> = listOf("night_club","bar","bowling_alley","cafe",
        "movie_theater","museum","restaurant","casino","park")
    private var initialDistance = 0.0F

    //Flags
    private var noPlace = true
    private var noRandomPlaceFound = true
    private var noActivityFoundCount = 1
    private var setInitialDistance = true
    private var placeAddedToDatabase = false

    private val viewModel: NavigationViewModel by lazy {
        ViewModelProvider(this).get(NavigationViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View {
        val binding: FragmentNavigationBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_navigation, container, false)

        binding.lifecycleOwner = this
        binding.navigationViewModel = viewModel // Giving the binding access to the ViewModel

        setCriteria() //Get search criteria settings
        viewModel.place.observe(viewLifecycleOwner, { place = it })
        viewModel.orientation.observe(viewLifecycleOwner, { orientation = it })
        viewModel.location.observe(viewLifecycleOwner, { location = it
            if (location.hasAccuracy()) {
                if (noPlace) getPlace() else {
                    insertPlaceIntoDatabase()
                    setDistance(binding)
                    setOrientation(binding)
                }
            } else noAccuracy()
        })
        return binding.root
    }

    //Sets the criteria from the criteria settings
    private fun setCriteria() {
        val criteria = PreferenceManager.getDefaultSharedPreferences(context)
        radius = criteria.getInt("criteriaDistanceSeekBar", 20).toString() + "000"
        val typesDefault = setOf("night_club", "bar", "bowling_alley", "cafe", "movie_theater", "museum", "restaurant", "casino", "park")
        val typesPreferences = criteria.getStringSet("multi_select_list_types", typesDefault )
        types = typesPreferences!!.shuffled()
        Log.i("navigation", "Search criteria: Radius = $radius, Type = $types")
    }

    //Tries to find the correct place to be navigated to
    private fun getPlace() {
        Log.i("navigation", "Searching place")
        val text = getString(R.string.navigation_searching_activity_toast)
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
        GlobalScope.launch {
            noPlace = if (viewModel.notFinishedActivity() != null) {
                false
            } else {
                val latLng = location.latitude.toString() + "," + location.longitude.toString()
                    types.forEach { i ->
                        if (noRandomPlaceFound) {
                            Log.i("navigation", "Searching for $i")
                            viewModel.getRandomPlace(latLng,
                                radius,
                                types.elementAt(types.indexOf(i)))
                            if (place.location != null) {
                                noRandomPlaceFound = false
                                Log.i("navigation", "Found a $i")
                            }
                        }
                    }
                false
            }
        }
        if (place.location != null) {
            noPlace = false
            Log.i("navigation", "place found: " + place.name)
        } else noActivityFound()
    }

    //Toast & navigateUp: Actions on no activity found
    private fun noActivityFound() {
        Log.i("navigation", "Searching activity attempt: $noActivityFoundCount")
        if (noActivityFoundCount > 2) {
            val text = getString(R.string.navigation_no_activity_found_toast)
            Toast.makeText(context, text, Toast.LENGTH_LONG).show()
            findNavController().navigateUp()
        } else noActivityFoundCount++
    }

    private fun insertPlaceIntoDatabase() {
        if (!placeAddedToDatabase) {
            viewModel.addToDatabase(place)
            Log.i("navigation", "Place added to database: ${place.name}")
            placeAddedToDatabase = true
        }
    }

    private fun setDistance(binding: FragmentNavigationBinding) {
        distance = location.distanceTo(place.location)
        if (setInitialDistance) {
            initialDistance = distance
            setInitialDistance = false
            Log.i("navigation", "Initial distance: $initialDistance")
        }
        binding.navigationDestinationImage.updatePaddingRelative(
        bottom = calculateDistanceImagePadding())

        if (distance < 50) nearbyActivity(binding)
    }

    //Returns the current local date and time
    private fun getDateTime() : LocalDateTime {
        val dateFormat = SimpleDateFormat(
            "yyyy-MM-dd", Locale.getDefault());
        val dateFormat2 = SimpleDateFormat(
            "HH:mm:ss", Locale.getDefault());
        val date = Date();
        Log.i("navigation", "LocalDateTime added to this place in the database: $date")
        return LocalDateTime.parse(dateFormat.format(date) + "T" + dateFormat2.format(date))
    }

    //Calculates the necessary padding for the distance image in the fragment ui
    private fun calculateDistanceImagePadding(): Int {
        //Calculates the percentage of the distance over the initialDistance less than 100%
        return if (distance < initialDistance) {
            val percentage = 100 * (distance / initialDistance)
            //Multiplying 1% of the total padding with the percentage minus 300
            (5.5 * percentage).toInt() - 300
        } else {
            if (distance > (initialDistance + 100)) {
                wrongDirection()
            }
            250 //returns 250 as max padding when the distance is over the initial distance
        }
    }



    //Actions on no accuracy found
    private fun noAccuracy() {
        Log.i("navigation", "No Accuracy: Show toast")
        val text = getString(R.string.navigation_no_accuracy_toast)
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    //Actions on wrong direction
    private fun wrongDirection() {
        Log.i("navigation", "Wrong direction: Show toast")
        val text = getString(R.string.navigation_wrong_direction_toast)
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    //Actions on within 50 meters of the activity
    private fun nearbyActivity(binding: FragmentNavigationBinding) {
        Log.i("navigation", "Nearby activity: Show toast & reveal button")
        val text = getString(R.string.navigation_nearby_activity_toast)
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        navigationRevealButton.visibility = View.VISIBLE
        binding.navigationRevealButton.setOnClickListener { v: View ->
            place.date = getDateTime()
            place.revealed = true
            viewModel.addToDatabase(place)
            noPlace = true
            v.findNavController().navigate(NavigationFragmentDirections
                .actionNavigationFragmentToPlaceFragment(place))
        }
    }

    private fun setOrientation(binding: FragmentNavigationBinding) {
        //Rotates the compass using the phones orientation
        binding.navigationCompassImage.rotation = 360F.minus(orientation)
        //Rotates the arrow using the destination and the orientation
        binding.navigationArrowImage.rotation =
            location.bearingTo(place.location).minus(orientation)
    }
}
