package org.gmetais.downloadmanager

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import org.gmetais.downloadmanager.databinding.BrowserBinding

class Browser : Fragment() {

    private lateinit var mBinding: BrowserBinding

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = BrowserBinding.inflate(inflater ?: LayoutInflater.from(activity))
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        RequestManager.browseRoot(this::update, this::onServiceFailure)
    }

    private fun update(directory: Directory) {
        mBinding.path = directory.Path
        val filesList = directory.Files
        val sb: StringBuilder = StringBuilder()
        for (file in filesList)
            sb.append(file.Path).append("\n")
        Toast.makeText(mBinding.root.context, sb.toString(), Toast.LENGTH_LONG).show()
    }

    private fun onServiceFailure(msg: String) {
        //TODO
    }
}
