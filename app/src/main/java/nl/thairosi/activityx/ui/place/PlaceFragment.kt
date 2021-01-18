package nl.thairosi.activityx.ui.place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import nl.thairosi.activityx.PlaceApplication
import nl.thairosi.activityx.R
import nl.thairosi.activityx.databinding.FragmentPlaceBinding
import nl.thairosi.activityx.utils.Utils

/**
 * This class inflates the place fragments to show place details to the user in the UI using
 * safeargs and Google API services via the PlaceRepository
 */
class PlaceFragment : Fragment() {

    private lateinit var viewModel: PlaceViewModel
    private val args: PlaceFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        // Binds the view for data Binding
        val binding: FragmentPlaceBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_place, container, false
        )

        // Binds the lifecycleOwner to this fragment
        binding.lifecycleOwner = this

        // Create an instance of the PlaceViewModel
        val placeRepository = (requireContext().applicationContext as PlaceApplication).placeRepository
        val viewModelProviderFactory = PlaceViewModelProviderFactory(placeRepository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(PlaceViewModel::class.java)

        // Giving the binding access to the ViewModel
        binding.placeViewModel = viewModel

        // Safeargs
        val place = args.place

        // Load the place data into the fragment using an API call
        viewModel.getPlaceFromAPI(place)

        // Load the place photo into the fragment with Glide
        viewModel.place.observe(viewLifecycleOwner, {
            if (it.photoReference.contains("null")) {
                binding.placeImage.visibility = View.VISIBLE
            } else {
                Glide.with(this)
                    .load(Utils.getImageUrl(it.photoReference))
                    .into(binding.placeImage)
                binding.placeImage.visibility = View.VISIBLE
            }
        })

        // Update place in room database when checkbox is clicked
        binding.placeBlockActivityCheckbox.setOnClickListener {
            place.blocked = binding.placeBlockActivityCheckbox.isChecked
            viewModel.updateOrInsertPlaceIntoDB(place)
        }

        // Return button navigates up
        binding.placeReturnButton.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }
}
