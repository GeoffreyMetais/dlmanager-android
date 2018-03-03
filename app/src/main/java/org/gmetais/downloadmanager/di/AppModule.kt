package org.gmetais.downloadmanager.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.multibindings.Multibinds
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun provideContext(application: Application) = application
}