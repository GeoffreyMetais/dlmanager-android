package org.gmetais.downloadmanager.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import org.gmetais.downloadmanager.NetworkHelper
import org.gmetais.downloadmanager.R

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {
private var alertDialog : AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NetworkHelper.getInstance(this).connected.observe(this, Observer<Boolean> { showNetworkDialog(it!!) })
    }

    override fun onStop() {
        super.onStop()
        alertDialog?.dismiss()
    }

    private fun showNetworkDialog(connected: Boolean) {
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) return
        val showing = alertDialog?.isShowing == true
        if (!connected) {
            if (showing) return
            if (alertDialog === null)
                alertDialog = AlertDialog.Builder(this)
                        .setTitle(getString(R.string.dialog_network_title))
                        .setMessage(getString(R.string.dialog_network_message))
                        .setPositiveButton(getString(android.R.string.ok)) { dialog, _ -> dialog.dismiss() }
                        .create()
            alertDialog?.show()
        } else if (showing) alertDialog?.dismiss()
    }
}