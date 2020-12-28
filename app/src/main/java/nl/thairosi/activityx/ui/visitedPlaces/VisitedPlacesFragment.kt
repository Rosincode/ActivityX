package nl.thairosi.activityx.ui.visitedPlaces

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import nl.thairosi.activityx.R
import nl.thairosi.activityx.databinding.FragmentVisitedPlacesBinding
import nl.thairosi.activityx.repository.PlaceRepository



class VisitedPlacesFragment : Fragment() {

    lateinit var viewModel: VisitedPlacesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentVisitedPlacesBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_visited_places, container, false)

        binding.nextButton.setOnClickListener { v: View ->
            v.findNavController().navigate(VisitedPlacesFragmentDirections.actionVisitedPlacesFragmentToPlaceFragment())
        }

        binding.lifecycleOwner = this

        val placeRepository = PlaceRepository()
        val viewModelProviderFactory = VisitedPlacesViewModelFactory(placeRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(VisitedPlacesViewModel::class.java)

        binding.visitedPlacesViewModel = viewModel

        return binding.root
    }
}