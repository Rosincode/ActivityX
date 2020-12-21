package nl.thairosi.activityx.ui.home

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import nl.thairosi.activityx.R
import nl.thairosi.activityx.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
            val binding: FragmentHomeBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_home,
                container,
                false)
            binding.goButton.setOnClickListener { v: View ->
            v.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToNavigationFragment())
        }
        return binding.root
    }

}