package org.gmetais.downloadmanager.ui

import dagger.Module
import dagger.Provides
import org.gmetais.downloadmanager.ui.helpers.NavigationListener

@Module
class MainActivityModule {
    @Provides
    fun provideNavigation(mainActivity: MainActivity): NavigationListener {
        return NavigationListener(mainActivity)
    }
}