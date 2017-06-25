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
                            .replace(org.gmetais.downloadmanager.R.id.fragment_placeholder, org.gmetais.downloadmanager.Browser(), "browser")
                            .commit()
                }
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

    override fun onBackPressed() {
        if ("shares".equals(supportFragmentManager.findFragmentById(R.id.fragment_placeholder)?.tag)) {
            finish()
            return
        }
        super.onBackPressed()
    }
}
