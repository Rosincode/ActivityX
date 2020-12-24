package nl.thairosi.activityx.ui.place

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import nl.thairosi.activityx.R
import nl.thairosi.activityx.databinding.FragmentPlaceBinding

class PlaceFragment : Fragment() {

    /**
     * Lazily initialize our [PlaceFragmentViewModel].
     */
    private val viewModel: PlaceViewModel by lazy {
        ViewModelProvider(this).get(PlaceViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentPlaceBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_place, container, false
        )

        binding.setLifecycleOwner(this)

        // Giving the binding access to the OverviewViewModel
        binding.placeViewModel = viewModel
//        binding.nextButton.setOnClickListener { v: View ->
//            v.findNavController().navigate(PlaceFragmentDirections.actionPlaceFragmentToHomeFragment())
//        }

        viewModel.photo.observe(viewLifecycleOwner, Observer { newUrl ->
            Picasso.get().load(newUrl).into(binding.placeImage)
        })

        return binding.root
    }

}
