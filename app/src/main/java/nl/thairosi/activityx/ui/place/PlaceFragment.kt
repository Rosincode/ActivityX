package nl.thairosi.activityx.ui.place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import nl.thairosi.activityx.R
import nl.thairosi.activityx.databinding.FragmentPlaceBinding

class PlaceFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentPlaceBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_place, container, false)
        binding.nextButton.setOnClickListener { v: View ->
            v.findNavController().navigate(PlaceFragmentDirections.actionPlaceFragmentToHomeFragment())
        }
        return binding.root
    }

}