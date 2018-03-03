package org.gmetais.downloadmanager.di

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import dagger.multibindings.Multibinds
import org.gmetais.downloadmanager.Application
import javax.inject.Singleton

@Component(modules = [(AndroidInjectionModule::class), (AndroidSupportInjectionModule::class),  (AppModule::class), (ActivityBuilder::class)])
interface AppComponent: AndroidInjector<DaggerApplication> {

    override fun inject(instance: DaggerApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: android.app.Application): Builder
        fun build(): AppComponent
    }
}