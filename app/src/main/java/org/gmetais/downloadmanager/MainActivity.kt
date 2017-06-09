package org.gmetais.downloadmanager

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    lateinit var mNavigation : BottomNavigationView
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                //TODO
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                //TODO
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
                    .add(R.id.fragment_placeholder, Browser(), "browser")
                    .addToBackStack("browser")
                    .commit()
        }
        mNavigation = findViewById(R.id.navigation)
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}
