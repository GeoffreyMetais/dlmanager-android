package org.gmetais.downloadmanager.ui

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.gmetais.downloadmanager.R
import org.gmetais.downloadmanager.ui.fragments.Browser
import org.gmetais.downloadmanager.ui.fragments.Preferences
import org.gmetais.downloadmanager.ui.fragments.SharesBrowser

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState === null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_placeholder, SharesBrowser(), "shares")
                    .commit()
        }
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
                supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_placeholder, SharesBrowser(), "shares")
                        .commit()
                true
            }
            R.id.navigation_browse -> {
                with(supportFragmentManager) {
                    if (popBackStackImmediate("root", android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE))
                        beginTransaction().remove(supportFragmentManager.findFragmentByTag("shares")).commit()
                    else
                        beginTransaction()
                                .replace(R.id.fragment_placeholder, Browser(), "browser")
                                .commit()
                }
                true
            }
            R.id.navigation_settings -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_placeholder, Preferences(), "settings")
                        .commit()
                true
            }
            else -> false
        }
    }
}
