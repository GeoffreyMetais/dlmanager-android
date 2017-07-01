package org.gmetais.downloadmanager

import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.gmetais.downloadmanager.databinding.BrowserBinding
import org.gmetais.downloadmanager.model.DirectoryModel

class Browser(val path : String? = null) : LifecycleFragment(), BrowserAdapter.IHandler {

    private lateinit var mBinding: BrowserBinding
    val mCurrentDirectory: DirectoryModel by lazy { ViewModelProviders.of(this).get(DirectoryModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = BrowserBinding.inflate(inflater)
        mBinding.filesList.layoutManager = LinearLayoutManager(mBinding.root.context)
        return mBinding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mCurrentDirectory.path = path
        mCurrentDirectory.directory.observe(this, Observer { update(it!!) })
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
