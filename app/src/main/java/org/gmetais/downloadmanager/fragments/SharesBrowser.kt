package org.gmetais.downloadmanager.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import org.gmetais.downloadmanager.SharedFile
import org.gmetais.downloadmanager.SharesAdapter
import org.gmetais.downloadmanager.model.SharesListModel


class SharesBrowser : BaseBrowser(), SharesAdapter.ShareHandler {

    val shares: SharesListModel by lazy { ViewModelProviders.of(activity).get(SharesListModel::class.java) }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.filesList.addItemDecoration(DividerItemDecoration(mBinding.filesList.context, DividerItemDecoration.VERTICAL))
        mBinding.filesList.adapter = SharesAdapter(this)
        shares.shares.observe(this, Observer<MutableList<SharedFile>> { update(it!!) })
        activity.title = "Shares"
        showProgress()
    }

    private fun update(shares: MutableList<SharedFile>) {
        showProgress(false)
        (mBinding.filesList.adapter as SharesAdapter).update(shares)
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
        showProgress()
        shares.delete(share)
    }

    fun onDelResponse(success: Boolean) {
        if (!success)
            Snackbar.make(mBinding.root, "failure", Snackbar.LENGTH_LONG).show()
    }
}