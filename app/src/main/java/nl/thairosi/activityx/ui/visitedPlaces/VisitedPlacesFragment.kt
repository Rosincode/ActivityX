package nl.thairosi.activityx.ui.navigation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import nl.thairosi.activityx.R

class VisitedPlacesFragment : Fragment(R.layout.fragment_visited_places) {

    private lateinit var viewModel: CriteriaViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CriteriaViewModel::class.java)
        // TODO: Use the ViewModel
    }

}