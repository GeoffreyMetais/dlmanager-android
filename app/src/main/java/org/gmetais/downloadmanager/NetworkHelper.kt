package org.gmetais.downloadmanager

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleActivity
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.*
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.v7.app.AlertDialog

class NetworkHelper : LifecycleObserver {

    private var activity: LifecycleActivity? = null
    private var mAlertDialog : AlertDialog? = null

    companion object {
        private val instance by lazy { NetworkHelper() }
        fun attach(activity: LifecycleActivity) {
            instance.activity = activity.apply { lifecycle.addObserver(instance) }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun start() = activity?.registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun stop() {
        mAlertDialog?.dismiss()
        mAlertDialog = null
        activity?.lifecycle?.removeObserver(this)
        activity?.unregisterReceiver(networkReceiver)
        activity = null
    }

    private fun showNetworkDialog() {
        val context = activity
        if (context === null|| !context.lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED))
            return
        if (mAlertDialog === null)
            mAlertDialog = AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.dialog_network_title))
                    .setMessage(context.getString(R.string.dialog_network_message))
                    .setPositiveButton(context.getString(android.R.string.ok),
                            {dialog, _ -> dialog.dismiss()})
                    .create()
        mAlertDialog!!.show()
    }

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (ConnectivityManager.CONNECTIVITY_ACTION == action) {
                val networkInfo = (Application.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
                if (networkInfo == null || (networkInfo.state != NetworkInfo.State.CONNECTED  && networkInfo.state != NetworkInfo.State.CONNECTING))
                    showNetworkDialog()
                else
                    mAlertDialog?.dismiss()
            }
        }
    }
}