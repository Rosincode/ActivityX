package nl.thairosi.activityx.ui.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import nl.thairosi.activityx.R
import nl.thairosi.activityx.databinding.FragmentNavigationBinding

class NavigationFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentNavigationBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_navigation, container, false)
        binding.nextButton.setOnClickListener { v: View ->
            v.findNavController().navigate(NavigationFragmentDirections.actionNavigationFragmentToPlaceFragment())
        }
        return binding.root
    }

}