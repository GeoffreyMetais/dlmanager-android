package org.gmetais.downloadmanager.ui.helpers

import android.support.design.widget.BottomNavigationView
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import org.gmetais.downloadmanager.R
import org.gmetais.downloadmanager.removeFragment
import org.gmetais.downloadmanager.replaceFragment
import org.gmetais.downloadmanager.ui.MainActivity
import org.gmetais.downloadmanager.ui.fragments.Browser
import org.gmetais.downloadmanager.ui.fragments.Preferences
import org.gmetais.downloadmanager.ui.fragments.SharesBrowser

class NavigationListener(private val activity: MainActivity): BottomNavigationView.OnNavigationItemSelectedListener {
    override fun onNavigationItemSelected(item: MenuItem) = when (item.itemId) {
        activity.navigation.selectedItemId -> false
        R.id.navigation_shares -> {
            activity.replaceFragment(R.id.fragment_placeholder, SharesBrowser(), "shares")
            true
        }
        R.id.navigation_browse -> {
            with(activity.supportFragmentManager) {
                if (popBackStackImmediate("root", android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE))
                    activity.removeFragment("shares")
                else
                    activity.replaceFragment(R.id.fragment_placeholder, Browser(), "root", true)
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