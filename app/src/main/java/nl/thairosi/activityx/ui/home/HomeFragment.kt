package nl.thairosi.activityx.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import nl.thairosi.activityx.R
import nl.thairosi.activityx.database.PlaceDatabase
import nl.thairosi.activityx.databinding.FragmentHomeBinding
import nl.thairosi.activityx.models.Place
import nl.thairosi.activityx.repository.PlaceRepository
import nl.thairosi.activityx.utils.Utils

/**
 * The HomeFragment is the first fragment that will be inflated when starting the application
 * This fragment contains functionality to navigate to start a new or continue a unfinished activity
 * This class also contains location permission requests when starting a new activity
 */
class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val binding: FragmentHomeBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false)

        binding.lifecycleOwner = this

        val placeRepository = PlaceRepository(PlaceDatabase(requireContext()))
        val viewModelProviderFactory = HomeViewModelProviderFactory(placeRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(HomeViewModel::class.java)

        // Giving the binding access to the ViewModel
        binding.homeViewModel = viewModel

        GlobalScope.launch {
            getDemoTestData().forEach {
                importDemoTestData(it)
            }
        }

        GlobalScope.launch {
            if (viewModel.getUnfinishedPlaceFromDB() != null) {
                binding.homeGoButton.visibility = View.INVISIBLE
                binding.homeContinueText.visibility = View.VISIBLE
                binding.homeContinueYesButton.visibility = View.VISIBLE
                binding.homeContinueNoButton.visibility = View.VISIBLE
            }
        }

        binding.homeContinueNoButton.setOnClickListener { v: View ->
            GlobalScope.launch { viewModel.deleteUnfinishedPlaceFromDB() }
            binding.homeGoButton.visibility = View.VISIBLE
            binding.homeContinueText.visibility = View.INVISIBLE
            binding.homeContinueYesButton.visibility = View.INVISIBLE
            binding.homeContinueNoButton.visibility = View.INVISIBLE
        }
        binding.homeContinueYesButton.setOnClickListener { v: View ->
            v.findNavController()
                .navigate(HomeFragmentDirections.actionHomeFragmentToNavigationFragment())
        }
        binding.homeGoButton.setOnClickListener { v: View ->
            val requestPermissionsList = mutableListOf<String>()
            requestPermissionsList.clear()
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionsList.add(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
            if (requestPermissionsList.isNotEmpty()) {
                requestPermissions(requestPermissionsList.toTypedArray(), 0)

            } else {
                v.findNavController()
                    .navigate(HomeFragmentDirections.actionHomeFragmentToNavigationFragment())
            }
        }

        return binding.root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults.isNotEmpty()) {
            var permissionGranted = true
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionGranted = false
                }
            }
            if (permissionGranted) {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToNavigationFragment())
            } else {
                Toast.makeText(context,
                    getString(R.string.home_ask_location_permission_toast_text),
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    // Will only be used for demo purposes, it will import five places into the database.
    private suspend fun importDemoTestData(place: Place) {
        viewModel.importDemoTestDataIntoDB(place)
    }

    // Will only be used for demo purposes, it will import five places into the database.
    private fun getDemoTestData(): MutableList<Place> {
        val placeList: MutableList<Place> = ArrayList()

        val placeA = Place(
            placeId = "ChIJ1yauVIGfxkcRMaisxu7BstM",
            name = "Park Breda",
            location = Utils.latAndLongToAndroidLocation(51.59130709999999, 4.7790085),
            revealed = true,
            date = Utils.getDateTime(),
            url = "https://maps.google.com/?cid=15254468119136872497",
            types = "park, tourist attraction, point of interest, establishment",
            address = "Catharinastraat 43, 4811 XE Breda, Netherlands",
            photoReference = "ATtYBwKaK5qlTyzsvWlNMRsnoE2n0Cu8q7r2ph0QZKmmmE_Q5K6HRT1k9pX82HGD9hf6TTpsywR6BS0Tt-OrnEp-Qb8e5XuNyj6TStW3so3e4puBg9O9KWj6t1a78WSqcao4fcmV9RN8iXE-FxU-w3RoFrWASyaRdliWhqD6b7U6tKDzSn4f"
        )

        val placeB = Place(
            placeId = "ChIJu8SZVoafxkcRfJUyfx0F5cs",
            name = "Memory Lane",
            location = Utils.latAndLongToAndroidLocation(51.5894843, 4.7751469),
            revealed = true,
            date = Utils.getDateTime(),
            url = "https://maps.google.com/?cid=14692154983612323196",
            types = "night club, point of interest, establishment",
            address = "Reigerstraat 22A, 4811 XB Breda, Netherlands",
            photoReference = "ATtYBwKfOlgAzAvUgYLs_GUnheuekA8E9-mhb6u4kcchCbwhahtqqcDIIIf2Q-Xjvrat7t9-SVmYZ5CirwpzHC1CN5u3LtjMY8lOF43uApaTvx9hX51miaAFfOvfKtpfEGiI_HEkSgi36EGXgrSrYExJjIXK6nYQPGqVlfIt5nOBfNvUvUEc"
        )

        val placeC = Place(
            placeId = "ChIJt7HDbymgxkcRePg63776TdY",
            name = "Charelli",
            location = Utils.latAndLongToAndroidLocation(51.5828964, 4.776437),
            revealed = true,
            date = Utils.getDateTime(),
            url = "https://maps.google.com/?cid=15442274395019212920",
            types = "restaurant, meal takeaway, cafe, food, point of interest, establishment",
            address = "Van Coothplein 35, 4811 NC Breda, Netherlands",
            photoReference = "ATtYBwJ9VfEZLLh1fodcR8o_fai5kt3dhGIL93eh4gjO3MyxsuM7U4YiPOXLU5m42lZ1x0TiUfwEJsuLjGyTVAi7GxuOIlEXf8aC8tVebBjNrqpDsZOcX3t_7WRBziRyN_CrHQvwq4dnLv1-t7_f4dw-S0RVKy5ladU-0vAbz6QG7l6XWQau"
        )

        placeList.add(placeA)
        placeList.add(placeB)
        placeList.add(placeC)

        return placeList
    }
}