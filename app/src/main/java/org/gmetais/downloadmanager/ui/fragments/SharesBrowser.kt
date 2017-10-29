package org.gmetais.downloadmanager.ui.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.view.View
import kotlinx.coroutines.experimental.launch
import org.gmetais.downloadmanager.data.SharedFile
import org.gmetais.downloadmanager.model.SharesListModel
import org.gmetais.downloadmanager.share
import org.gmetais.downloadmanager.ui.adapters.SharesAdapter

class SharesBrowser : BaseBrowser(), SharesAdapter.ShareHandler {

    private val shares: SharesListModel by lazy { ViewModelProviders.of(activity!!).get(SharesListModel::class.java) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.filesList.addItemDecoration(DividerItemDecoration(mBinding.filesList.context, DividerItemDecoration.VERTICAL))
        mBinding.filesList.adapter = SharesAdapter(this)
        launch { shares.dataResult.observe(this@SharesBrowser, Observer<List<SharedFile>> { update(it!!) }) }
        activity?.title = "Shares"
        showProgress()
    }

    private fun update(list: List<SharedFile>) {
        showProgress(false)
        (mBinding.filesList.adapter as SharesAdapter).update(list)
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