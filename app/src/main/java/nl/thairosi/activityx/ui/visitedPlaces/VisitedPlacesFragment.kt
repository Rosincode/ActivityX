package nl.thairosi.activityx.ui.visitedPlaces

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_visited_places.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nl.thairosi.activityx.PlaceApplication
import nl.thairosi.activityx.R
import nl.thairosi.activityx.adapters.VisitedPlacesAdapter
import nl.thairosi.activityx.databinding.FragmentVisitedPlacesBinding
import nl.thairosi.activityx.repository.PlaceRepository

/**
 * Provides a list of visited places to the user in the UI using a recycler view
 * The user can use a switch to block and unblock visited places
 * Also the user can view more details when he opens a visited place
 */
class VisitedPlacesFragment : Fragment() {

    lateinit var viewModel: VisitedPlacesViewModel
    lateinit var visitedPlacesAdapter: VisitedPlacesAdapter
    lateinit var binding: FragmentVisitedPlacesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        // Binds the view for data Binding
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_visited_places, container, false)

        // Binds the lifecycleOwner to this fragment
        binding.lifecycleOwner = this

        // Create an instance of the PlaceViewModel
        val placeRepository = (requireContext().applicationContext as PlaceApplication).taskRepository
        val viewModelProviderFactory = VisitedPlacesViewModelProviderFactory(placeRepository as PlaceRepository)
        viewModel = ViewModelProvider(this,
            viewModelProviderFactory).get(VisitedPlacesViewModel::class.java)

        // Giving the binding access to the ViewModel
        binding.visitedPlacesViewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        // Gets visitedPlaces from DB and pass it to the RecyclerView adapter
        binding.progressBar.visibility = View.VISIBLE
        viewModel.getVisitedPlacesFromDB().observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                binding.textViewEmpty.visibility = View.VISIBLE
            }
            visitedPlacesAdapter.differ.submitList(it)
            binding.progressBar.visibility = View.INVISIBLE
        })

        // OnClickListener for Items, to navigate to placeFragment
        visitedPlacesAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putParcelable("place", it)
            }
            findNavController().navigate(
                R.id.action_visitedPlacesFragment_to_placeFragment,
                bundle)
        }

        // OnClickListener to update database when toggle "blocked" is changed
        visitedPlacesAdapter.setOnToggleClickListener {
            println(it.blocked)
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.updateOrInsert(it)
            }
        }
    }

    // Helper method to setup the recyclerView
    private fun setupRecyclerView() {
        visitedPlacesAdapter = VisitedPlacesAdapter()
        recycler_view.apply {
            adapter = visitedPlacesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}