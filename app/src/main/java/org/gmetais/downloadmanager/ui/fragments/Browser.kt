package org.gmetais.downloadmanager.ui.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import org.gmetais.downloadmanager.R
import org.gmetais.downloadmanager.data.*
import org.gmetais.downloadmanager.getNameFromPath
import org.gmetais.downloadmanager.model.DirectoryModel
import org.gmetais.downloadmanager.putStringExtra
import org.gmetais.downloadmanager.replaceFragment
import org.gmetais.downloadmanager.ui.LinkCreatorDialog
import org.gmetais.downloadmanager.ui.adapters.BrowserAdapter

class Browser : BaseBrowser(), BrowserAdapter.IHandler {

    private val mCurrentDirectory: DirectoryModel by lazy { ViewModelProviders.of(this, DirectoryModel.Factory(arguments?.getString("path"))).get(DirectoryModel::class.java) }
    private lateinit var searchItem : MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPause() {
        super.onPause()
        searchItem.collapseActionView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.filesList.adapter = BrowserAdapter(this)
        activity?.title = arguments?.getString("path")?.getNameFromPath() ?: "root"
        mCurrentDirectory.dataResult.observe(this, Observer<Result> { update(it!!) })
        showProgress()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.browser, menu)
        searchItem = menu.findItem(R.id.ml_menu_filter)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "search…"
        searchView.setOnQueryTextListener(mCurrentDirectory)
        searchItem.setOnActionExpandListener(mCurrentDirectory)
    }

    private fun update(result: Result) {
        showProgress(false)
        @Suppress("UNCHECKED_CAST")
        when (result) {
            is Success<*> -> (mBinding.filesList.adapter as BrowserAdapter).update((result.content as Directory).files.sortedBy { !it.isDirectory })
            is Error -> Snackbar.make(mBinding.filesList, result.message, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun open(file: File) {
        if (file.isDirectory)
            activity?.replaceFragment(R.id.fragment_placeholder, Browser().putStringExtra("path", file.path), file.path.getNameFromPath(), true)
        else
            LinkCreatorDialog().putStringExtra("path", file.path).show(activity?.supportFragmentManager, "linkin park")
    }

    override fun onRefresh() {
        mCurrentDirectory.refresh()
    }
}
