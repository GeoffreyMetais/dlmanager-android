package org.gmetais.downloadmanager

import androidx.lifecycle.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.databinding.ObservableBoolean
import android.net.ConnectivityManager
import android.net.NetworkInfo

object NetworkHelper : BroadcastReceiver(), LifecycleObserver {

    val disconnected = MutableLiveData<Boolean>()
    val connected = ObservableBoolean(true)

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun register(): Intent = Application.instance.registerReceiver(this, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun unregister() = Application.instance.unregisterReceiver(this)

    override fun onReceive(context: Context, intent: Intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION == intent.action) {
            val networkInfo = (Application.context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.activeNetworkInfo
            connected.set(networkInfo?.state == NetworkInfo.State.CONNECTED  || networkInfo?.state == NetworkInfo.State.CONNECTING)
            disconnected.value = !connected.get()
        }
    }
}