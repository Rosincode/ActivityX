package nl.thairosi.activityx.ui.navigation

import android.annotation.SuppressLint
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
                .navigate(NavigationFragmentDirections.actionNavigationFragmentToPlaceFragment())
        }

        viewModel.navigation.observe(viewLifecycleOwner, { navigation ->
            binding.navigationArrow.rotation = navigation.rotation
            binding.NavigationDistanceText.text = navigation.distance.toString()
        })

        return binding.root
    }



    private fun calculateDistance(): String {
        return 100.toString()
    }

}