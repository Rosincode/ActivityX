package nl.thairosi.activityx.ui.place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.squareup.picasso.Picasso
import nl.thairosi.activityx.R
import nl.thairosi.activityx.databinding.FragmentPlaceBinding
import nl.thairosi.activityx.repository.PlaceRepository

class PlaceFragment : Fragment() {

    lateinit var viewModel: PlaceViewModel

//    private val viewModel: PlaceViewModel by lazy {
//        ViewModelProvider(this).get(PlaceViewModel::class.java)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentPlaceBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_place, container, false
        )

        binding.lifecycleOwner = this

        val placeRepository = PlaceRepository()
        val viewModelProviderFactory = PlaceViewModelProviderFactory(placeRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(PlaceViewModel::class.java)

        // Giving the binding access to the ViewModel
        binding.placeViewModel = viewModel

        // Load the place photo into the fragment with Picasso
        viewModel.place.observe(viewLifecycleOwner, { place ->
            Picasso.get().load(place.photo).into(binding.placeImage)
        })

        // Let the return button navigate to homeFragment
        binding.placeReturnButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_placeFragment_to_homeFragment)
        )

        return binding.root
    }

}
