package org.gmetais.downloadmanager.ui

import android.os.Bundle
import org.gmetais.downloadmanager.R
import org.gmetais.downloadmanager.addFragment
import org.gmetais.downloadmanager.databinding.ActivityMainBinding
import org.gmetais.downloadmanager.ui.fragments.SharesBrowser
import org.gmetais.downloadmanager.ui.helpers.NavigationListener

class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        savedInstanceState ?: addFragment(R.id.fragment_placeholder, SharesBrowser(), "shares")
        binding.navigation.setOnNavigationItemSelectedListener(NavigationListener(this))
    }

    override fun onBackPressed() {
        if ("shares" == supportFragmentManager.findFragmentById(R.id.fragment_placeholder)?.tag) {
            finish()
            return
        }
        super.onBackPressed()
    }
}
