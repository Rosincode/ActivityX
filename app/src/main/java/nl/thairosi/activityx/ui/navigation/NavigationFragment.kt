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
import nl.thairosi.activityx.preferences.Preferences
import nl.thairosi.activityx.utils.Utils

/**
 * This NavigationFragment is the base of the application because it provides the user with all
 * functionality to navigate to a randomly found activity
 *
 * This fragment uses a viewModel to access external data for example to observe live location
 * and orientation values
 */
class NavigationFragment : Fragment() {

    // Properties
    private lateinit var radius: String
    private lateinit var types: List<String>
    private lateinit var location: Location
    private var place: Place = Place()
    private var orientation: Float = 0F
    private var distance: Float = 10000F
    private var initialDistance: Float = 0.0F

    // Flags
    private var placeFound = false
    private var placeNotFound = false
    private var searching = false
    private var setInitialDistance = true
    private var placeAddedToDatabase = false
    private var nearbyActivity = false
    private var informedAboutWrongDirection = false
    private var informedAboutActivityFound = false

    private val viewModel: NavigationViewModel by lazy {
        ViewModelProvider(this).get(NavigationViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding: FragmentNavigationBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_navigation, container, false)

        binding.lifecycleOwner = this

        binding.navigationViewModel = viewModel // Giving the binding access to the ViewModel

        val preferenceManager = PreferenceManager.getDefaultSharedPreferences(context)

        loading()

        radius = Preferences.getRadius(preferenceManager)
        types = Preferences.getTypes(preferenceManager)

        viewModel.place.observe(viewLifecycleOwner, {
            place = it
        })

        viewModel.location.observe(viewLifecycleOwner, {
            location = it
            if (!placeFound && !searching && location.hasAccuracy()) {
                searching = true
                getAndSetPlace()
            }
            if (placeFound) calculateAndSetDistance(binding)
            if (placeNotFound) noActivityFound()
        })

        viewModel.orientation.observe(viewLifecycleOwner, {
            orientation = it
            if (placeFound) rotateCompassAndArrow(binding)
        })

        return binding.root
    }

    // Shows a toast to inform the user that the application is loading
    private fun loading() {
        val loadingSettingsToastText = getString(R.string.navigation_loading_toast)
        Toast.makeText(context, loadingSettingsToastText, Toast.LENGTH_SHORT).show()
    }

    // Tries to find a existing or random place to be navigated to
    private fun getAndSetPlace() {
        val searchingActivityToastText = getString(R.string.navigation_searching_activity_toast)
        Toast.makeText(context, searchingActivityToastText, Toast.LENGTH_SHORT).show()
        if (this::location.isInitialized && radius.isNotBlank() &&
            types.isNotEmpty() && location.hasAccuracy()) {
            GlobalScope.launch {
                placeFound = if (viewModel.getUnfinishedActivityFromDB() != null) true else {
                    searchRandomPlace()
                    insertPlaceIntoDatabase()
                    true
                }
            }
        }
    }

    // Searches a random place using the search criteria
    private suspend fun searchRandomPlace() {
        val latLng = location.latitude.toString() + "," + location.longitude.toString()
        types.forEach { i ->
            if (place.location == null) {
                Log.i("navigation", "searching for a $i")
                viewModel.getRandomPlaceFromAPI(latLng, radius, types.elementAt(types.indexOf(i)))
                delay(2000)
            }
        }
        if (place.location == null) placeNotFound = true
    }

    // Actions on no activity found
    private fun noActivityFound() {
        Log.i("navigation", "No activity found")
        val noActivityFoundToastText = getString(R.string.navigation_no_activity_found_toast)
        Toast.makeText(context, noActivityFoundToastText, Toast.LENGTH_LONG).show()
        findNavController().navigateUp()
    }

    // When the place is not yet inserted into the database it will be done when it is found
    private fun insertPlaceIntoDatabase() {
        if (!placeAddedToDatabase) {
            if (place.location != null) {
                viewModel.updateOrInsertPlaceIntoDB(place)
                Log.i("navigation", "Place added to database: ${place.name}")
                placeAddedToDatabase = true
            }
        }
        searching = false
    }

    // Calculates and sets the current distance when a place is found
    private fun calculateAndSetDistance(binding: FragmentNavigationBinding) {
        if (!informedAboutActivityFound) {
            val activityFoundToastText = getString(R.string.navigation_activity_found_toast)
            Toast.makeText(context, activityFoundToastText, Toast.LENGTH_SHORT).show()
            informedAboutActivityFound = true
        }
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
            if (distance > (initialDistance + 100)) wrongDirection()
        }
    }

    // Calculates the necessary padding for the distance image in the fragment UI
    private fun calculateDistanceImagePadding(): Int {
        return if (distance < initialDistance) {
            Utils.percentageToValue(550F,
                Utils.valueToPercentage(distance, initialDistance)).toInt() - 300
        } else 250
    }

    // Orientates the compass and the bearing arrow in the UI
    private fun rotateCompassAndArrow(binding: FragmentNavigationBinding) {
        if (place.location != null && this::location.isInitialized) {
            binding.navigationCompassImage.rotation = 360F.minus(orientation)
            binding.navigationArrowImage.rotation =
                location.bearingTo(place.location).minus(orientation)
        }
    }

    // Actions on within 50 meters of the activity
    private fun nearbyActivity(binding: FragmentNavigationBinding) {
        if (!nearbyActivity) {
            nearbyActivity = true
            Log.i("navigation", "Nearby activity: Show toast & reveal button")
            val nearbyActivityToastText = getString(R.string.navigation_nearby_activity_toast)
            Toast.makeText(context, nearbyActivityToastText, Toast.LENGTH_LONG).show()
            navigationRevealButton.visibility = View.VISIBLE
            binding.navigationRevealButton.setOnClickListener { v: View ->
                place.date = Utils.getDateTime()
                place.revealed = true
                viewModel.updateOrInsertPlaceIntoDB(place)
                placeFound = false
                v.findNavController().navigate(NavigationFragmentDirections
                    .actionNavigationFragmentToPlaceFragment(place))
            }
        }
    }

    // Actions on wrong direction
    private fun wrongDirection() {
        if (!informedAboutWrongDirection) {
            Log.i("navigation", "Wrong direction")
            val wrongDirectionToastText = getString(R.string.navigation_wrong_direction_toast)
            Toast.makeText(context, wrongDirectionToastText, Toast.LENGTH_LONG).show()
            informedAboutWrongDirection = true
        }
    }
}
