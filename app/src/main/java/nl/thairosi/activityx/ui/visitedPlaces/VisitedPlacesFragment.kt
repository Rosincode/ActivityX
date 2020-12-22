package nl.thairosi.activityx.ui.visitedPlaces

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import nl.thairosi.activityx.R
import nl.thairosi.activityx.databinding.FragmentVisitedPlacesBinding

class VisitedPlacesFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentVisitedPlacesBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_visited_places, container, false)
        binding.nextButton.setOnClickListener { v: View ->
            v.findNavController().navigate(VisitedPlacesFragmentDirections.actionVisitedPlacesFragmentToPlaceFragment())
        }
        return binding.root
    }
}