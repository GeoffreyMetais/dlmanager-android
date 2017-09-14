package org.gmetais.downloadmanager

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.OnLifecycleEvent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import java.lang.ref.WeakReference

class NetworkHelper : LifecycleObserver {

    private lateinit var controller: WeakReference<NetworkController>

    interface NetworkController {
        fun getLifecycle() : LifecycleRegistry
        fun registerReceiver(receiver: BroadcastReceiver, filter: IntentFilter) : Intent
        fun unregisterReceiver(receiver: BroadcastReceiver)
        fun onConnectionChanged(disconnected: Boolean)
    }

    companion object {
        private val instance = NetworkHelper()
        fun attach(controller: NetworkController) {
            instance.controller = WeakReference(controller.apply { getLifecycle().addObserver(instance) })
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        controller.get()?.registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        controller.get()?.getLifecycle()?.removeObserver(this)
        controller.get()?.unregisterReceiver(networkReceiver)
        controller.clear()
    }

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION == intent.action) {
                val networkInfo = (Application.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
                controller.get()?.onConnectionChanged(networkInfo == null || (networkInfo.state != NetworkInfo.State.CONNECTED  && networkInfo.state != NetworkInfo.State.CONNECTING))
            }
        }
    }
}