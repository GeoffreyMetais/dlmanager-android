package org.gmetais.downloadmanager

import android.annotation.SuppressLint
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

class NetworkHelper : LifecycleObserver {

    private var controller: NetworkController? = null

    interface NetworkController {
        fun getLifecycle() : LifecycleRegistry
        fun registerReceiver(receiver: BroadcastReceiver, filter: IntentFilter) : Intent
        fun unregisterReceiver(receiver: BroadcastReceiver)
        fun onConnectionChanged(disconnected: Boolean)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private val instance = NetworkHelper()
        fun attach(controller: NetworkController) {
            instance.controller = controller.apply { getLifecycle().addObserver(instance) }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        controller?.registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        controller?.getLifecycle()?.removeObserver(this)
        controller?.unregisterReceiver(networkReceiver)
        controller = null
    }

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION == intent.action) {
                val networkInfo = (Application.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
                controller?.onConnectionChanged(networkInfo == null || (networkInfo.state != NetworkInfo.State.CONNECTED  && networkInfo.state != NetworkInfo.State.CONNECTING))
            }
        }
    }
}