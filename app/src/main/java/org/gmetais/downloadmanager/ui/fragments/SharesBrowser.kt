package org.gmetais.downloadmanager.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.gmetais.downloadmanager.data.SharedFile
import org.gmetais.downloadmanager.model.SharesListModel
import org.gmetais.downloadmanager.share
import org.gmetais.downloadmanager.ui.adapters.SharesAdapter

@UseExperimental(ObsoleteCoroutinesApi::class)
@ExperimentalCoroutinesApi
class SharesBrowser : BaseBrowser(), SharesAdapter.ShareHandler {

    private val shares: SharesListModel by lazy { ViewModelProviders.of(this).get(SharesListModel::class.java) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.filesList.addItemDecoration(DividerItemDecoration(binding.filesList.context, DividerItemDecoration.VERTICAL))
        binding.filesList.adapter = SharesAdapter(this)
        shares.dataResult.observe(this@SharesBrowser, Observer<List<SharedFile>> { update(it) })
        shares.exception.observe(this@SharesBrowser, Observer { onError(it?.getContent()) })
        activity?.title = "Shares"
        showProgress()
    }

    private fun update(list: List<SharedFile>?) = list?.run {
        showProgress(false)
        (binding.filesList.adapter as SharesAdapter).update(list)
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