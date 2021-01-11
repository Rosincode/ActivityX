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
import nl.thairosi.activityx.repository.PlaceRepository

class HomeFragment : Fragment() {

    lateinit var viewModel: HomeViewModel

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
            if (viewModel.notFinishedActivity() != null) {
                binding.homeGoButton.visibility = View.INVISIBLE
                binding.homeContinueText.visibility = View.VISIBLE
                binding.homeContinueYesButton.visibility = View.VISIBLE
                binding.homeContinueNoButton.visibility = View.VISIBLE
            }
        }

        binding.homeContinueNoButton.setOnClickListener { v: View ->
            GlobalScope.launch {
                viewModel.deleteNotFinishedActivity()
            }
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
                    "In order to get an activity, the app needs your permission to use your location.",
                    Toast.LENGTH_LONG).show()
            }
        }
    }
}