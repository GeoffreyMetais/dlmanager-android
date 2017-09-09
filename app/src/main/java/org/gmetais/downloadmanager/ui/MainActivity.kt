package org.gmetais.downloadmanager.ui

import android.arch.lifecycle.LifecycleActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import org.gmetais.downloadmanager.*
import org.gmetais.downloadmanager.ui.fragments.Browser
import org.gmetais.downloadmanager.ui.fragments.Preferences
import org.gmetais.downloadmanager.ui.fragments.SharesBrowser

class MainActivity : LifecycleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NetworkHelper.attach(this)
        setContentView(R.layout.activity_main)

        if (savedInstanceState === null)
            addFragment(R.id.fragment_placeholder, SharesBrowser(), "shares")
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    override fun onBackPressed() {
        if ("shares" == supportFragmentManager.findFragmentById(R.id.fragment_placeholder)?.tag) {
            finish()
            return
        }
        super.onBackPressed()
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            navigation.selectedItemId -> false
            R.id.navigation_shares -> {
                replaceFragment(R.id.fragment_placeholder, SharesBrowser(), "shares")
                true
            }
            R.id.navigation_browse -> {
                with(supportFragmentManager) {
                    if (popBackStackImmediate("root", android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE))
                        removeFragment("shares")
                    else
                        replaceFragment(R.id.fragment_placeholder, Browser(), "browser")
                }
                true
            }
            R.id.navigation_settings -> {
                replaceFragment(R.id.fragment_placeholder, Preferences(), "settings")
                true
            }
            else -> false
        }
    }
}
