package nl.thairosi.activityx.ui.criteria

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import nl.thairosi.activityx.R

/**
 * This Fragment extends the PreferenceFragmentCompat in order to save shared preferences
 * This class inflates the criteria fragment to give the user control over the preferences in the UI
 */
class CriteriaFragment : PreferenceFragmentCompat() {

    // Called to supply the preferences for this fragment
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_criteria, rootKey)
    }

}