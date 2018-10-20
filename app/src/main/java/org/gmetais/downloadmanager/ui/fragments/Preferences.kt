package org.gmetais.downloadmanager.ui.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import android.view.View
import org.gmetais.downloadmanager.R

class Preferences : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.settings)
    }
}