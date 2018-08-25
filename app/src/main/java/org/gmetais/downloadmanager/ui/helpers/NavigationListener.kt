package org.gmetais.downloadmanager.ui.helpers

import android.support.design.widget.BottomNavigationView
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import org.gmetais.downloadmanager.R
import org.gmetais.downloadmanager.getFragment
import org.gmetais.downloadmanager.replaceFragment
import org.gmetais.downloadmanager.ui.MainActivity
import org.gmetais.downloadmanager.ui.fragments.Browser
import org.gmetais.downloadmanager.ui.fragments.Preferences
import org.gmetais.downloadmanager.ui.fragments.SharesBrowser

private const val TAG = "NavigationListener"

class NavigationListener(private val activity: MainActivity): BottomNavigationView.OnNavigationItemSelectedListener {
    override fun onNavigationItemSelected(item: MenuItem) = when (item.itemId) {
        activity.navigation.selectedItemId -> false
        R.id.navigation_shares -> {
            activity.replaceFragment(R.id.fragment_placeholder, SharesBrowser(), "shares")
            true
        }
        R.id.navigation_browse -> {
            with(activity.supportFragmentManager) {
                val fragment = activity.getFragment("root") ?: Browser()
                activity.replaceFragment(R.id.fragment_placeholder, fragment, "root", false)
            }
            true
        }
        R.id.navigation_settings -> {
            activity.replaceFragment(R.id.fragment_placeholder, Preferences(), "settings")
            true
        }
        else -> false
    }
}