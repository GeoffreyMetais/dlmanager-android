package org.gmetais.downloadmanager

import android.annotation.SuppressLint
import androidx.room.Room
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import org.gmetais.downloadmanager.data.SharesDatabase

@SuppressLint("StaticFieldLeak")
class Application : android.app.Application() {

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
}