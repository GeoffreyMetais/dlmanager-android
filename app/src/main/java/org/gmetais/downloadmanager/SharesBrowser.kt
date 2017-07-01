package org.gmetais.downloadmanager

import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.gmetais.downloadmanager.databinding.BrowserBinding
import org.gmetais.downloadmanager.model.SharesListModel


class SharesBrowser : LifecycleFragment(), SharesAdapter.ShareHandler {

    private lateinit var mBinding: BrowserBinding
    val shares: SharesListModel by lazy { ViewModelProviders.of(activity).get(SharesListModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = BrowserBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shares.getSharesList().observe(this, Observer<MutableList<SharedFile>> { update(it!!) })
        mBinding.filesList.layoutManager = LinearLayoutManager(mBinding.root.context)
        mBinding.filesList.addItemDecoration(DividerItemDecoration(mBinding.filesList.context, DividerItemDecoration.VERTICAL))
    }

    private fun update(shares: MutableList<SharedFile>) {
        activity.title = "Shares"
        mBinding.filesList.adapter = SharesAdapter(this, shares)
    }

    private fun onServiceFailure(msg: String) {
        Snackbar.make(mBinding.root, msg, Snackbar.LENGTH_LONG).show()
    }

    override fun open(share: SharedFile) {
        startActivity(Intent(Intent.ACTION_SEND)
                .putExtra(Intent.EXTRA_TEXT, share.link)
                .setType("text/plain"))
    }

    override fun delete(share: SharedFile) {
        shares.delete(share)
    }

    fun onDelResponse(name: String, success: Boolean) {
        if (success)
            (mBinding.filesList.adapter as SharesAdapter).remove(name)
        else
            Snackbar.make(mBinding.root, "failure", Snackbar.LENGTH_LONG).show()
    }
}