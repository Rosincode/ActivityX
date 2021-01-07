package nl.thairosi.activityx.ui.home

import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import kotlinx.coroutines.*
import nl.thairosi.activityx.R
import nl.thairosi.activityx.database.PlaceDatabase
import nl.thairosi.activityx.databinding.FragmentHomeBinding
import nl.thairosi.activityx.models.Place
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

        viewModel.place.observe(viewLifecycleOwner, { place ->
            if (place.placeId != "") {
                binding.homeGoButton.visibility = View.INVISIBLE
                binding.homeContinueText.visibility = View.VISIBLE
                binding.homeContinueYesButton.visibility = View.VISIBLE
                binding.homeContinueNoButton.visibility = View.VISIBLE

                binding.homeContinueNoButton.setOnClickListener { v: View ->
                    viewModel.deleteNotFinishedActivity()
                    binding.homeGoButton.visibility = View.VISIBLE
                    binding.homeContinueText.visibility = View.INVISIBLE
                    binding.homeContinueYesButton.visibility = View.INVISIBLE
                    binding.homeContinueNoButton.visibility = View.INVISIBLE

                }
            }
        })

        binding.homeGoButton.setOnClickListener { v: View ->
            v.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToNavigationFragment())
        }
        return binding.root
    }
}