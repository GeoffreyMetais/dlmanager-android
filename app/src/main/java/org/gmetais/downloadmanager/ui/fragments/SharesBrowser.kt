package org.gmetais.downloadmanager.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.gmetais.downloadmanager.data.SharedFile
import org.gmetais.downloadmanager.model.SharesListModel
import org.gmetais.downloadmanager.share
import org.gmetais.downloadmanager.ui.adapters.SharesAdapter
import org.gmetais.tools.Click
import org.gmetais.tools.ImageClick
import org.gmetais.tools.SimpleClick

@UseExperimental(ObsoleteCoroutinesApi::class)
@ExperimentalCoroutinesApi
class SharesBrowser : BaseBrowser() {

    private val shares: SharesListModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.filesList.addItemDecoration(DividerItemDecoration(binding.filesList.context, DividerItemDecoration.VERTICAL))
        val adapter = SharesAdapter()
        binding.filesList.adapter = adapter
        shares.dataResult.observe(viewLifecycleOwner, Observer<List<SharedFile>> { update(it) })
        shares.exception.observe(viewLifecycleOwner, Observer { onError(it?.getContent()) })
        activity?.title = "Shares"
        showProgress()
        adapter.events.onEach { it.process() }.launchIn(lifecycleScope)
    }

    private fun update(list: List<SharedFile>?) = list?.run {
        showProgress(false)
        (binding.filesList.adapter as SharesAdapter).update(list)
    }

    override fun onRefresh() {
        shares.refresh()
    }

    private fun Click.process() {
        val share = shares.dataResult.value?.get(position) ?: return
        when(this) {
            is SimpleClick -> activity?.share(share)
            is ImageClick -> {
                showProgress()
                shares.delete(share)
            }
        }
    }
}
