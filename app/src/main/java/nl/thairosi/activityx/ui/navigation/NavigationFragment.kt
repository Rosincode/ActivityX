package nl.thairosi.activityx.ui.navigation

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.window.Popup
import androidx.core.view.updatePaddingRelative
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_navigation.*
import nl.thairosi.activityx.R
import nl.thairosi.activityx.databinding.FragmentNavigationBinding
import nl.thairosi.activityx.models.Place
import java.time.LocalDateTime


class NavigationFragment : Fragment() {

    private val viewModel: NavigationViewModel by lazy {
        ViewModelProvider(this).get(NavigationViewModel::class.java)
    }

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val binding: FragmentNavigationBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_navigation, container, false
        )

        binding.lifecycleOwner = this

        // Giving the binding access to the ViewModel
        binding.navigationViewModel = viewModel

        binding.navigationRevealButton.setOnClickListener { v: View ->
            v.findNavController()
                .navigate(NavigationFragmentDirections.actionNavigationFragmentToPlaceFragment(
                    Place(
                        "ChIJW5MOkVpvxkcRDYqgo2pLGBY",
                        "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=ATtYBwK4_gAWP97d78u_JjiTe6TimhiICXvYF0Ww2NlM--JD3CagpFHv2s13vRAkjKSvXG4hg2Tum4ecFPUrB1BDaOQZJX8eT2fUP3inYE6kqKwItZM0qJ6Hgm9QfX2VSzPRbFozclkwvh1kFFPPU_FwEmbd-ibzotmAntN936IXa6DxCP_n&key=AIzaSyCofXZEndG5WokT6i6n5fdMabW3IWkGiRc",
                        "Kafé België",
                        "Oudegracht 196, 3511 NR Utrecht, Netherlands",
                        "cafe, bar, restaurant, food, point of interest, establishment",
                        "https://maps.google.com/?cid=1592105389659294221",
                        locationConverter(),
                        date = LocalDateTime.parse("2021-01-04T18:50:53"),
                        true)))
        }

        //Flags to determine what functionality is to be executed within the observers
        var findRandomPlace = true
        var noActivityFoundCount = 0
        var logAddress = true
        var setInitialDistance = true
        var initialDistance = 0.0F

        //LOCATION OBSERVATION
        viewModel.location.observe(viewLifecycleOwner, { location ->
            if (location.hasAccuracy()) {
                //Uses the phones location and the search criteria to get a random place or not
                if (findRandomPlace) {
                    searchingActivity()
                    val latLng = location.latitude.toString() + "," + location.longitude.toString()
                    viewModel.getRandomPlace(latLng, "1500", "cafe", "4")
                    findRandomPlace = false
                } else {
                    //PLACE OBSERVATION
                    viewModel.place.observe(viewLifecycleOwner, { place ->
                        if (place.location != null) {
                            val distance = location.distanceTo(place.location)

                            //Set the initial distance for percentage calculations
                            if (setInitialDistance) {
                                initialDistance = distance
                                setInitialDistance = false
                            }

                            //Logs the randomly found address for demo purposes
                            if (logAddress) {
                                Log.i("navigation", place.name)
                                logAddress = false
                            }

                            //Updates the bottom padding of the distance image
                            binding.navigationDestinationImage.updatePaddingRelative(
                                bottom = calculateDistanceImagePadding(
                                    initialDistance,
                                    distance
                                )
                            )

                            //Informs the user he is nearby the activity shows the reveal button
                            if (distance < 50) {
                                nearbyActivity()
                            }

                            //ORIENTATION OBSERVATION
                            viewModel.orientation.observe(viewLifecycleOwner, { orientation ->
                                //Rotates the compass using the phones orientation
                                binding.navigationCompassImage.rotation = 360F.minus(orientation)
                                //Rotates the arrow using the destination and the orientation
                                binding.navigationArrowImage.rotation =
                                    location.bearingTo(place.location).minus(orientation)
                            })
                        } else {
                            noActivityFound(noActivityFoundCount)
                            noActivityFoundCount++
                            Log.i("navigation", noActivityFoundCount.toString())
                        }
                    })
                }
            } else {
                noAccuracy()
            }
        })
        return binding.root
    }

    //Calculates the necessary padding for the distance image in the fragment ui
    private fun calculateDistanceImagePadding(initialDistance: Float, distance: Float): Int {
        //Calculates the percentage of the distance over the initialDistance less than 100%
        return if (distance < initialDistance) {
            val percentage = 100 * (distance / initialDistance)
            //Multiplying 1% of the total padding with the percentage minus 300
            (5.5 * percentage).toInt() - 300
        } else {
            if (distance > (initialDistance + 100)) {
                wrongDirection()
            }
            250 //returns 250 when over 100% as the max padding
        }
    }

    //To test
    private fun locationConverter(): Location {
        val androidLocation = Location(LocationManager.GPS_PROVIDER)
        androidLocation.latitude = 52.08915712791165
        androidLocation.longitude = 5.12157871534323
        //
        return androidLocation
    }
    private fun searchingActivity() {
        val text = "Searching activity"
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }

    //Actions on no activity found
    private fun noActivityFound(noActivityFoundCount: Int) {
        if (noActivityFoundCount > 1) {
            val text = "No activity found, please adjust your search criteria"
            Toast.makeText(context, text, Toast.LENGTH_LONG).show()
            findNavController().navigateUp()
        }
    }

    //Actions on no accuracy found
    private fun noAccuracy() {
        val text = "Waiting for location accuracy"
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    //Actions on wrong direction
    private fun wrongDirection() {
        val text = "You are moving away from the activity"
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    //Actions on within 50 meters of the activity
    private fun nearbyActivity() {
        val text = "You are now nearby your activity!"
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
        navigationRevealButton.visibility = View.VISIBLE
    }
}
