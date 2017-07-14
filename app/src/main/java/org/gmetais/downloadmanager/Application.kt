package org.gmetais.downloadmanager

import android.app.Application

class Application : Application() {

    companion object {
        lateinit var instance : org.gmetais.downloadmanager.Application
        fun getContext() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}