package org.gmetais.downloadmanager

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.gmetais.downloadmanager.databinding.BrowserBinding

class Browser : Fragment() {

    private lateinit var mBinding: BrowserBinding

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = BrowserBinding.inflate(inflater ?: LayoutInflater.from(activity))
        mBinding.folderPath.text = "chemin du dossier"
        return mBinding.root
    }
}
