package org.gmetais.downloadmanager

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.gmetais.downloadmanager.databinding.BrowserBinding


class SharesBrowser : Fragment(), SharesAdapter.ShareHandler {

    private lateinit var mBinding: BrowserBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = BrowserBinding.inflate(inflater)
        mBinding.filesList.layoutManager = LinearLayoutManager(mBinding.root.context)
        return mBinding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        RequestManager.listShares(this::update, this::onServiceFailure)
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
        RequestManager.delete(share.name, this::onDelResponse)
    }

    fun onDelResponse(name: String, success: Boolean) {
        if (success)
            (mBinding.filesList.adapter as SharesAdapter).remove(name)
        else
            Snackbar.make(mBinding.root, "failure", Snackbar.LENGTH_LONG).show()
    }
}