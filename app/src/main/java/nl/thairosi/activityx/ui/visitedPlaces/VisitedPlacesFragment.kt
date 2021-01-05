package nl.thairosi.activityx.ui.visitedPlaces

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_visited_places.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nl.thairosi.activityx.R
import nl.thairosi.activityx.adapters.VisitedPlacesAdapter
import nl.thairosi.activityx.database.PlaceDatabase
import nl.thairosi.activityx.databinding.FragmentVisitedPlacesBinding
import nl.thairosi.activityx.models.Place
import nl.thairosi.activityx.repository.PlaceRepository



class VisitedPlacesFragment : Fragment() {

    lateinit var viewModel: VisitedPlacesViewModel
    lateinit var visitedPlacesAdapter: VisitedPlacesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentVisitedPlacesBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_visited_places, container, false)



//        binding.nextButton.setOnClickListener { v: View ->
//            v.findNavController().navigate(VisitedPlacesFragmentDirections.actionVisitedPlacesFragmentToPlaceFragment(
//                Place(
//                "ChIJW5MOkVpvxkcRDYqgo2pLGBY",
//                "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=ATtYBwK4_gAWP97d78u_JjiTe6TimhiICXvYF0Ww2NlM--JD3CagpFHv2s13vRAkjKSvXG4hg2Tum4ecFPUrB1BDaOQZJX8eT2fUP3inYE6kqKwItZM0qJ6Hgm9QfX2VSzPRbFozclkwvh1kFFPPU_FwEmbd-ibzotmAntN936IXa6DxCP_n&key=AIzaSyCofXZEndG5WokT6i6n5fdMabW3IWkGiRc",
//                "Kafé België",
//                "Oudegracht 196, 3511 NR Utrecht, Netherlands",
//                "cafe, bar, restaurant, food, point of interest, establishment",
//                "https://maps.google.com/?cid=1592105389659294221",
//                "",
//                true)))
//        }


        binding.lifecycleOwner = this

        val placeRepository = PlaceRepository(PlaceDatabase(requireContext()))
        val viewModelProviderFactory = VisitedPlacesViewModelFactory(placeRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(VisitedPlacesViewModel::class.java)

        binding.visitedPlacesViewModel = viewModel








        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()



        viewModel.getVisitedPlaces().observe(viewLifecycleOwner, Observer {
            visitedPlacesAdapter.differ.submitList(it)
        })


        visitedPlacesAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putParcelable("place", it)
            }
            findNavController().navigate(
                R.id.action_visitedPlacesFragment_to_placeFragment,
                bundle)
        }
        visitedPlacesAdapter.setOnToggleClickListener {
                // TO DO
                println(it.blocked)
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.updateOrInsert(it)
            }

        }

    }

    private fun setupRecyclerView() {
        visitedPlacesAdapter = VisitedPlacesAdapter()
        recycler_view.apply {
            adapter = visitedPlacesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}