package org.gmetais.downloadmanager

import android.app.Activity
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.View

class MainActivity : AppCompatActivity() {

    val mNavigation by bind<BottomNavigationView>(R.id.navigation)

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_shares -> {
                supportFragmentManager.fragments.clear()
                supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_placeholder, SharesBrowser(), "shares")
                        .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_browse -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_placeholder, Browser(), "browser")
                        .commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
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

    fun <T : View> Activity.bind(@IdRes res : Int) : Lazy<T> {
        return lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(res) }
    }
}
