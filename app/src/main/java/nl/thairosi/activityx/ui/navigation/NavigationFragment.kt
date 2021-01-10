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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nl.thairosi.activityx.R
import nl.thairosi.activityx.databinding.FragmentNavigationBinding
import nl.thairosi.activityx.models.Place
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class NavigationFragment : Fragment() {
    //Properties
    private lateinit var radius: String
    private lateinit var types: List<String>
    private lateinit var location: Location
    private var place: Place = Place()
    private var orientation: Float = 0F
    private var distance: Float = 10000F
    private var initialDistance = 0.0F

    //Flags
    private var placeFound = false
    private var placeNotFound = false
    private var randomPlaceSearchStarted = false
    private var setInitialDistance = true
    private var placeAddedToDatabase = false
    private var nearbyActivity = false
    private var informedAboutWrongDirection = false

    private val viewModel: NavigationViewModel by lazy {
        ViewModelProvider(this).get(NavigationViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View {
        val binding: FragmentNavigationBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_navigation, container, false)

        binding.lifecycleOwner = this

        binding.navigationViewModel = viewModel // Giving the binding access to the ViewModel

        getAndSetCriteria() //Get search criteria settings

        viewModel.place.observe(viewLifecycleOwner, {
            place = it
        })

        viewModel.location.observe(viewLifecycleOwner, {
            location = it
            if (!placeFound) getAndSetPlace()
            if (placeFound) calculateAndSetDistances(binding)
            if (placeNotFound) noActivityFound()
        })

        viewModel.orientation.observe(viewLifecycleOwner, {
            orientation = it
            if (placeFound) rotateCompassAndArrow(binding)
        })

        return binding.root
    }

    //Gets and sets the criteria from the criteria settings
    private fun getAndSetCriteria() {
        val criteria = PreferenceManager.getDefaultSharedPreferences(context)
        radius = criteria.getInt("criteriaDistanceSeekBar", 2).toString() + "000"
        val typesDefault = setOf("night_club", "bar", "bowling_alley", "cafe", "movie_theater",
            "museum", "restaurant", "casino", "park")
        var typesPreferences = criteria.getStringSet("multi_select_list_types", typesDefault)
        if (typesPreferences.isNullOrEmpty()) {
            Log.i("navigation", "Criteria: No types set so all types are used")
            typesPreferences = typesDefault
        }
        types = typesPreferences.shuffled()
        Log.i("navigation", "Criteria: Types = $types")
        Log.i("navigation", "Criteria: Radius = $radius meter")
    }

    //Tries to find a existing or random place to be navigated to
    private fun getAndSetPlace() {
        val text = getString(R.string.navigation_searching_activity_toast)
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
        if (this::location.isInitialized && this::radius.isInitialized &&
            this::types.isInitialized && location.hasAccuracy()) {
            GlobalScope.launch {
                placeFound = if (viewModel.notFinishedActivity() != null) true else {
                    searchRandomPlace()
                    insertPlaceIntoDatabase()
                    true
                }
            }
        }
    }

    //Searches a random place using the search criteria
    private suspend fun searchRandomPlace() {
        if (!randomPlaceSearchStarted) {
            randomPlaceSearchStarted = true
            val latLng = location.latitude.toString() + "," + location.longitude.toString()
            types.forEach { i ->
                if (place.location == null) {
                    Log.i("navigation", "searching for a $i")
                    viewModel.getRandomPlace(latLng, radius, types.elementAt(types.indexOf(i)))
                    delay(1000)
                }
            }
            if (place.location == null) placeNotFound = true
        }
    }

    //Toast & navigateUp: Actions on no activity found
    private fun noActivityFound() {
        Log.i("navigation", "No activity found")
        val text = getString(R.string.navigation_no_activity_found_toast)
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
        findNavController().navigateUp()
    }

    //When the place is not yet inserted into the database it will be done when it is found
    private fun insertPlaceIntoDatabase() {
        if (!placeAddedToDatabase) {
            if (place.location != null) {
                viewModel.addToDatabase(place)
                Log.i("navigation", "Place added to database: ${place.name}")
                placeAddedToDatabase = true
            }
        }
    }

    //Calculates and sets the current distance when a place is found
    private fun calculateAndSetDistances(binding: FragmentNavigationBinding) {
        if (place.location != null) {
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
    }

    //Returns the current local date and time
    private fun getDateTime(): LocalDateTime {
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

    //Orientates the compass and the bearing arrow in the UI
    private fun rotateCompassAndArrow(binding: FragmentNavigationBinding) {
        if (place.location != null && this::location.isInitialized) {
            binding.navigationCompassImage.rotation = 360F.minus(orientation)
            binding.navigationArrowImage.rotation =
                location.bearingTo(place.location).minus(orientation)
        }
    }

    //Actions on within 50 meters of the activity
    private fun nearbyActivity(binding: FragmentNavigationBinding) {
        if (!nearbyActivity) {
            nearbyActivity = true
            Log.i("navigation", "Nearby activity: Show toast & reveal button")
            val text = getString(R.string.navigation_nearby_activity_toast)
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
            navigationRevealButton.visibility = View.VISIBLE
            binding.navigationRevealButton.setOnClickListener { v: View ->
                place.date = getDateTime()
                place.revealed = true
                viewModel.addToDatabase(place)
                placeFound = false
                v.findNavController().navigate(NavigationFragmentDirections
                    .actionNavigationFragmentToPlaceFragment(place))
            }
        }
    }

    //Actions on wrong direction
    private fun wrongDirection() {
        if (!informedAboutWrongDirection) {
            Log.i("navigation", "Wrong direction")
            val text = getString(R.string.navigation_wrong_direction_toast)
            Toast.makeText(context, text, Toast.LENGTH_LONG).show()
            informedAboutWrongDirection = true
        }
    }
}
