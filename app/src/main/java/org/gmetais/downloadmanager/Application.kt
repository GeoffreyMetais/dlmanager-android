package org.gmetais.downloadmanager

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.app.AppCompatDelegate
import dagger.android.support.DaggerApplication
import org.gmetais.downloadmanager.di.DaggerAppComponent


@SuppressLint("StaticFieldLeak")
class Application : DaggerApplication() {

    companion object {
        lateinit var instance : Application
        lateinit var context : Context
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        context = applicationContext
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    override fun applicationInjector()= DaggerAppComponent.builder().application(this).build()
}