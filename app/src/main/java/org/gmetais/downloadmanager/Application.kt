package org.gmetais.downloadmanager

import android.support.v7.app.AppCompatDelegate

class Application : android.app.Application() {

    companion object {
        lateinit var instance : Application
        fun getContext() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }
}