package nl.thairosi.activityx.ui.home

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import nl.thairosi.activityx.R
import nl.thairosi.activityx.database.PlaceDatabase
import nl.thairosi.activityx.databinding.FragmentHomeBinding
import nl.thairosi.activityx.repository.PlaceRepository
import nl.thairosi.activityx.ui.place.PlaceViewModel
import nl.thairosi.activityx.ui.place.PlaceViewModelProviderFactory

class HomeFragment : Fragment() {

    lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
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



            binding.homeGoButton.setOnClickListener { v: View ->
            v.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToNavigationFragment())
        }
        return binding.root
    }

}