package org.gmetais.downloadmanager

class Application : android.app.Application() {

    companion object {
        lateinit var instance : Application
        fun getContext() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}