package nl.thairosi.activityx.ui.navigation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import nl.thairosi.activityx.R

class CriteriaFragment : Fragment(R.layout.fragment_criteria) {

    private lateinit var viewModel: CriteriaViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CriteriaViewModel::class.java)
        // TODO: Use the ViewModel
    }

}