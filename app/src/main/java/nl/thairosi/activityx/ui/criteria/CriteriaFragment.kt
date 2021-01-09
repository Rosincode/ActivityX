package nl.thairosi.activityx.ui.criteria

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import nl.thairosi.activityx.R

class CriteriaFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_criteria, rootKey)
    }

}