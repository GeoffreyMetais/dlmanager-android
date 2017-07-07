package org.gmetais.downloadmanager.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import org.gmetais.downloadmanager.*
import org.gmetais.downloadmanager.model.DirectoryModel

class Browser(val path : String? = null) : BaseBrowser(), BrowserAdapter.IHandler {

    val mCurrentDirectory: DirectoryModel by lazy { ViewModelProviders.of(this, DirectoryModel.Factory(path)).get(DirectoryModel::class.java) }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mCurrentDirectory.directory.observe(this, Observer { update(it!!) })
        showProgress()
    }

    private fun update(directory: Directory) {
        showProgress(false)
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
