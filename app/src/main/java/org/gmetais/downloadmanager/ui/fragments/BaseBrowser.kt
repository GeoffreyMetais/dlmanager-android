package org.gmetais.downloadmanager.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.gmetais.downloadmanager.databinding.BrowserBinding

abstract class BaseBrowser : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    abstract override fun onRefresh()

    protected lateinit var binding : BrowserBinding
    private val mHandler: Handler by lazy { Handler(Looper.getMainLooper()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = BrowserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.swiperefresh.setOnRefreshListener(this)
        binding.filesList.layoutManager = LinearLayoutManager(view.context)
    }

    fun showProgress(show: Boolean = true) {
        if (show)
            mHandler.postDelayed({binding.swiperefresh.isRefreshing = true}, 300)
        else {
            mHandler.removeCallbacksAndMessages(null)
            binding.swiperefresh.isRefreshing = false
        }
    }

    protected fun onError(exception: Exception?) {
        showProgress(false)
        exception?.let { Snackbar.make(binding.filesList, it.message ?: it.localizedMessage, Snackbar.LENGTH_LONG).show() }
    }
}