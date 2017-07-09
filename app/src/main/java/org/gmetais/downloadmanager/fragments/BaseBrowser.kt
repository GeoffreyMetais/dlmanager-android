package org.gmetais.downloadmanager.fragments

import android.arch.lifecycle.LifecycleFragment
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.gmetais.downloadmanager.databinding.BrowserBinding

open class BaseBrowser : LifecycleFragment() {

    protected lateinit var mBinding: BrowserBinding
    protected val mHandler: Handler by lazy { Handler(Looper.getMainLooper()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = BrowserBinding.inflate(inflater)
        mBinding.filesList.layoutManager = LinearLayoutManager(mBinding.root.context)
        return mBinding.root
    }

    fun showProgress(show: Boolean = true) {
        if (show)
            mHandler.postDelayed({mBinding.sharesProgress.visibility = View.VISIBLE}, 300)
        else {
            mHandler.removeCallbacksAndMessages(null)
            mBinding.sharesProgress.visibility = View.GONE
        }
    }
}