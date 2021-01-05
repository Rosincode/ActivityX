package nl.thairosi.activityx.ui.navigation

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.view.updatePaddingRelative
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import nl.thairosi.activityx.R
import nl.thairosi.activityx.databinding.FragmentNavigationBinding
import nl.thairosi.activityx.models.Place
import java.sql.Date
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

        binding.nextButton.setOnClickListener { v: View ->
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

        var getLocation = true
        var printLatLng = true
        viewModel.location.observe(viewLifecycleOwner, { location ->
            if(location.hasAccuracy()) {
                if (getLocation) {
                    val latLng = location.latitude.toString() + "," + location.longitude.toString()
                    viewModel.getRandomPlace(latLng, "2000", "cafe", "4")
                    getLocation = false
                }
                if (!getLocation) {
                    viewModel.place.observe(viewLifecycleOwner, { place ->
                        if (place.location != null) {
                            if (printLatLng) {
                                Log.i("randomPlace", place.name)
                                printLatLng = false
                            }
                            //Calculates the distance to the destination in meters
                            val distance = location.distanceTo(place.location)
                            //Sets the initial padding for the destinationImage to 250 (used above 2000m)
                            var distancePaddingBottom = 250
                            //Calculates the necessary paddingBottom for the destinationImage within 2000m
                            if (distance < 2000) {
                                //Calculates the percentage of the distance (2000m is 100%)
                                val percentage = 0.05 * distance
                                //Multiplying 1% of the total padding with the percentage
                                distancePaddingBottom = (5.5 * percentage).toInt() - 300
                            }
                            viewModel.orientation.observe(viewLifecycleOwner, { orientation ->
                                binding.navigationCompassImage.rotation = 360F.minus(orientation)
                                binding.navigationArrowImage.rotation =
                                    location.bearingTo(place.location).minus(orientation)
                                binding.navigationDestinationImage.updatePaddingRelative(0, 0, 0,
                                    distancePaddingBottom)
                            })
                        }
                    })
                }
            }
        })

        return binding.root

    }
        // To test
        private fun locationConverter(): Location {
            val androidLocation = Location(LocationManager.GPS_PROVIDER)
            androidLocation.latitude = 52.08915712791165
            androidLocation.longitude = 5.12157871534323
            //
            return androidLocation
        }

    }
