package org.gmetais.downloadmanager.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import org.gmetais.downloadmanager.ui.MainActivity
import org.gmetais.downloadmanager.ui.MainActivityModule

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = [(MainActivityModule::class)])
    abstract fun bindMainActivity(): MainActivity

}