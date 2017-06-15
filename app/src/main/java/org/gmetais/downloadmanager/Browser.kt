package org.gmetais.downloadmanager

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.gmetais.downloadmanager.databinding.BrowserBinding

class Browser : Fragment() {

    private lateinit var mBinding: BrowserBinding

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = BrowserBinding.inflate(inflater ?: LayoutInflater.from(activity))
        mBinding.filesList.layoutManager = LinearLayoutManager(mBinding.root.context)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        RequestManager.browseRoot(this::update, this::onServiceFailure)
    }

    private fun update(directory: Directory) {
        mBinding.path = directory.Path
        mBinding.filesList.adapter = BrowserAdapter(directory.Files.sortedBy { !it.IsDir })
    }

    private fun onServiceFailure(msg: String) {
        //TODO
    }
}
