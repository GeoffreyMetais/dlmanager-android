package org.gmetais.downloadmanager.ui

import android.annotation.SuppressLint
import android.arch.lifecycle.Lifecycle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import org.gmetais.downloadmanager.NetworkHelper
import org.gmetais.downloadmanager.R

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity(), NetworkHelper.NetworkController {
    private var mAlertDialog : AlertDialog? = null

    override fun onStop() {
        super.onStop()
        mAlertDialog?.dismiss()
    }

    override fun onConnectionChanged(disconnected: Boolean) = showNetworkDialog(disconnected)

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
            mAlertDialog!!.show()
        } else if (showing)
            mAlertDialog!!.dismiss()
    }
}