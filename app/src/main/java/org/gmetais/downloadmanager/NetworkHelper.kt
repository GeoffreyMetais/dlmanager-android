@file:Suppress("DEPRECATION", "unused")

package org.gmetais.downloadmanager

import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.*
import org.gmetais.tools.SingletonHolder

private const val TAG = "NetworkHelper"

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class NetworkHelper(private val ctx: Context) : LifecycleObserver {

    val connected = MutableLiveData<Boolean>().apply { value = false }
    var cm : ConnectivityManager? = null

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun register() {
        cm = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager else null
        cm?.apply {
            registerNetworkCallback(NetworkRequest.Builder().build(), networkCallback)
        } ?: Application.instance.registerReceiver(br, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun unregister() = cm?.apply { unregisterNetworkCallback(networkCallback) } ?: Application.instance.unregisterReceiver(br)

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network?) {
            connected.postValue(true)
        }

        override fun onLost(network: Network?) {
            connected.postValue(false)
        }
    }

    private val br = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (ConnectivityManager.CONNECTIVITY_ACTION == intent?.action) {
                val networkInfo = (ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.activeNetworkInfo
                connected.value = networkInfo?.state == NetworkInfo.State.CONNECTED || networkInfo?.state == NetworkInfo.State.CONNECTING
            }
        }
    }

    companion object : SingletonHolder<NetworkHelper, Context>({ NetworkHelper(it.applicationContext) })
}