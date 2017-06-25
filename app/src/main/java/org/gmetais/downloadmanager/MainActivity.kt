package org.gmetais.downloadmanager

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    val mNavigation by bind<BottomNavigationView>(R.id.navigation)

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            mNavigation.selectedItemId -> false
            R.id.navigation_shares -> {
                supportFragmentManager.fragments.clear()
                supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_placeholder, SharesBrowser(), "shares")
                        .commit()
                true
            }
            R.id.navigation_browse -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_placeholder, Browser(), "browser")
                        .commit()
                true
            }
            else -> false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_placeholder, SharesBrowser(), "shares")
                    .commit()
        }
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}
