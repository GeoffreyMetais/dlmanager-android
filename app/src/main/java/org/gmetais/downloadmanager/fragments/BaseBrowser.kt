package org.gmetais.downloadmanager.fragments

import android.arch.lifecycle.LifecycleFragment
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.gmetais.downloadmanager.R
import org.gmetais.downloadmanager.bind

abstract class BaseBrowser : LifecycleFragment(), SwipeRefreshLayout.OnRefreshListener {

    abstract override fun onRefresh()

    protected val mFilesList by bind<RecyclerView>(R.id.files_list)
    protected val mSwipeRefreshLayout by bind<SwipeRefreshLayout>(R.id.swiperefresh)
    protected val mHandler: Handler by lazy { Handler(Looper.getMainLooper()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.browser, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mSwipeRefreshLayout.setOnRefreshListener(this)
        mFilesList.layoutManager = LinearLayoutManager(view.context)
    }

    fun showProgress(show: Boolean = true) {
        if (show)
            mHandler.postDelayed({mSwipeRefreshLayout.isRefreshing = true}, 300)
        else {
            mHandler.removeCallbacksAndMessages(null)
            mSwipeRefreshLayout.isRefreshing = false
        }
    }
}