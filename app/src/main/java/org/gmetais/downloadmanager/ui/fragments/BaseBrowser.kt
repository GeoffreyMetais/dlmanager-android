package org.gmetais.downloadmanager.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.gmetais.downloadmanager.databinding.BrowserBinding

abstract class BaseBrowser : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    abstract override fun onRefresh()

    lateinit var mBinding : BrowserBinding
    private val mHandler: Handler by lazy { Handler(Looper.getMainLooper()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = BrowserBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mBinding.swiperefresh.setOnRefreshListener(this)
        mBinding.filesList.layoutManager = LinearLayoutManager(view.context)
    }

    fun showProgress(show: Boolean = true) {
        if (show)
            mHandler.postDelayed({mBinding.swiperefresh.isRefreshing = true}, 300)
        else {
            mHandler.removeCallbacksAndMessages(null)
            mBinding.swiperefresh.isRefreshing = false
        }
    }
}