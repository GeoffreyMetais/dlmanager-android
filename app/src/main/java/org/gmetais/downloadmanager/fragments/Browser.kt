package org.gmetais.downloadmanager.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import org.gmetais.downloadmanager.*
import org.gmetais.downloadmanager.model.BaseModel
import org.gmetais.downloadmanager.model.DirectoryModel

class Browser : BaseBrowser(), BrowserAdapter.IHandler {

    val mCurrentDirectory: DirectoryModel by lazy { ViewModelProviders.of(this, DirectoryModel.Factory(arguments?.getString("path"))).get(DirectoryModel::class.java) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity.title = arguments?.getString("path")?.getNameFromPath() ?: "root"
        mCurrentDirectory.dataResult.observe(this, Observer { update(it!!) })
        showProgress()
    }

    private fun update(result: BaseModel.Result) {
        showProgress(false)
        @Suppress("UNCHECKED_CAST")
        when (result) {
            is BaseModel.Result.Success<*> -> mFilesList.adapter = BrowserAdapter(this, (result.content as Directory).files.sortedBy { !it.isDirectory })
            is BaseModel.Result.Error -> Snackbar.make(mFilesList, result.message, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun open(file: File) {
        if (file.isDirectory) {
            activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_placeholder, Browser().putStringExtra("path", file.path), file.path.getNameFromPath())
                    .addToBackStack(activity.title.toString())
                    .commit()
        } else {
            val linkCreatorDialog = LinkCreatorDialog()
            val args = Bundle(1)
            args.putString("path", file.path)
            linkCreatorDialog.arguments = args
            linkCreatorDialog.show(activity.supportFragmentManager, "linking park")
        }
    }

    override fun onRefresh() {
        mCurrentDirectory.loadData()
    }
}
