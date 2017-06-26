package org.gmetais.downloadmanager

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.gmetais.downloadmanager.databinding.BrowserBinding

class Browser(var path : String? = null) : Fragment(), BrowserAdapter.IHandler {

    private lateinit var mBinding: BrowserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState !== null)
            path = savedInstanceState.getString("path")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = BrowserBinding.inflate(inflater)
        mBinding.filesList.layoutManager = LinearLayoutManager(mBinding.root.context)
        return mBinding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        RequestManager.browse(path, this::update, this::onServiceFailure)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString("path", path)
    }

    private fun update(directory: Directory) {
        activity.title = directory.path.getNameFromPath()
        mBinding.filesList.adapter = BrowserAdapter(this, directory.files.sortedBy { !it.isDirectory })
    }

    private fun onServiceFailure(msg: String) {
        Snackbar.make(mBinding.root, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun open(file: File) {
        if (file.isDirectory) {
            activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_placeholder, Browser(file.path), file.path.getNameFromPath())
                    .addToBackStack(this.path?.getNameFromPath() ?: "root")
                    .commit()
        } else {
            val linkCreatorDialog = LinkCreatorDialog()
            val args = Bundle(1)
            args.putString("path", file.path)
            linkCreatorDialog.arguments = args
            linkCreatorDialog.show(activity.supportFragmentManager, "linking park")
        }
    }
}
