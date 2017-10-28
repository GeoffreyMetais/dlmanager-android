package org.gmetais.downloadmanager.ui.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.DividerItemDecoration
import android.view.View
import org.gmetais.downloadmanager.data.Error
import org.gmetais.downloadmanager.data.Result
import org.gmetais.downloadmanager.data.SharedFile
import org.gmetais.downloadmanager.data.Success
import org.gmetais.downloadmanager.model.SharesListModel
import org.gmetais.downloadmanager.share
import org.gmetais.downloadmanager.ui.adapters.SharesAdapter

class SharesBrowser : BaseBrowser(), SharesAdapter.ShareHandler {

    private val shares: SharesListModel by lazy { ViewModelProviders.of(activity).get(SharesListModel::class.java) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.filesList.addItemDecoration(DividerItemDecoration(mBinding.filesList.context, DividerItemDecoration.VERTICAL))
        mBinding.filesList.adapter = SharesAdapter(this)
        shares.dataResult.observe(this, Observer<Result> { update(it!!) })
        activity.title = "Shares"
        showProgress()
    }

    private fun update(result: Result) {
        showProgress(false)
        @Suppress("UNCHECKED_CAST")
        when (result) {
            is Success<*> -> (mBinding.filesList.adapter as SharesAdapter).update(result.content as List<SharedFile>)
            is Error -> Snackbar.make(mBinding.filesList, result.message, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun open(share: SharedFile) = activity?.share(share)

    override fun delete(share: SharedFile) {
        showProgress()
        shares.delete(share)
    }

    override fun onRefresh() {
        shares.refresh()
    }
}