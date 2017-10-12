package org.gmetais.downloadmanager.ui

import android.annotation.SuppressLint
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import org.gmetais.downloadmanager.NetworkHelper
import org.gmetais.downloadmanager.R
import kotlin.properties.Delegates

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {
    private var mAlertDialog by Delegates.observable<AlertDialog?>(null) { _, _, new -> new?.show() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NetworkHelper.disconnected.observe(this, Observer<Boolean> { showNetworkDialog(it!!) })
    }

    override fun onStop() {
        super.onStop()
        mAlertDialog?.dismiss()
    }

    private fun showNetworkDialog(disconnected: Boolean) {
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED))
            return
        val showing = mAlertDialog?.isShowing == true
        if (disconnected) {
            if (showing)
                return
            if (mAlertDialog === null)
                mAlertDialog = AlertDialog.Builder(this)
                        .setTitle(getString(R.string.dialog_network_title))
                        .setMessage(getString(R.string.dialog_network_message))
                        .setPositiveButton(getString(android.R.string.ok), { dialog, _ -> dialog.dismiss() })
                        .create()
        } else if (showing)
            mAlertDialog!!.dismiss()
    }
}