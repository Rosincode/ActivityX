package nl.thairosi.activityx.ui.navigation

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import nl.thairosi.activityx.R
import nl.thairosi.activityx.databinding.FragmentNavigationBinding
import nl.thairosi.activityx.models.Place
import java.sql.Date


class NavigationFragment : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val viewModel: NavigationViewModel by lazy {
        ViewModelProvider(this).get(NavigationViewModel::class.java)
    }

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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
                        Date(2020,12,20),
                        true)))
        }

        viewModel.navigation.observe(viewLifecycleOwner, { navigation ->
            binding.navigationArrow.rotation = navigation.rotation
            binding.NavigationDistanceText.text = navigation.distance.toString()
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



    private fun calculateDistance(): String {
        return 100.toString()
    }

}