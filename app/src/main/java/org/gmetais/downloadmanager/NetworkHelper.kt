package org.gmetais.downloadmanager

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import java.lang.ref.WeakReference

class NetworkHelper : BroadcastReceiver(), ActivityLifecycleCallbacks by EmptyALC {

    private lateinit var controller: WeakReference<NetworkController>

    interface NetworkController {
        fun onConnectionChanged(disconnected: Boolean)
    }

    companion object {
        private val instance = NetworkHelper()
        fun get() = instance
    }

    override fun onActivityStarted(activity: Activity) {
        if (activity is NetworkController)
            controller = WeakReference(activity)
        activity.registerReceiver(this, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onActivityStopped(activity: Activity) = activity.unregisterReceiver(this)

    override fun onReceive(context: Context, intent: Intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION == intent.action) {
            val networkInfo = (Application.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.activeNetworkInfo
            controller.get()?.onConnectionChanged(networkInfo?.state != NetworkInfo.State.CONNECTED  && networkInfo?.state != NetworkInfo.State.CONNECTING)
        }
    }
}