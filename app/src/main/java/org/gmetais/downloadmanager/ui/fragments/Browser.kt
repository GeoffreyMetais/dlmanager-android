package org.gmetais.downloadmanager.ui.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.gmetais.downloadmanager.R
import org.gmetais.downloadmanager.data.Directory
import org.gmetais.downloadmanager.data.File
import org.gmetais.downloadmanager.getNameFromPath
import org.gmetais.downloadmanager.model.DirectoryModel
import org.gmetais.downloadmanager.putStringExtra
import org.gmetais.downloadmanager.replaceFragment
import org.gmetais.downloadmanager.ui.LinkCreatorDialog
import org.gmetais.downloadmanager.ui.adapters.BrowserAdapter
import org.gmetais.downloadmanager.ui.adapters.PathAdapter

class Browser : BaseBrowser(), BrowserAdapter.IHandler {

    private val directoryModel: DirectoryModel by lazy { ViewModelProviders.of(this, DirectoryModel.Factory(arguments?.getString("path"))).get(DirectoryModel::class.java) }
    private lateinit var searchItem : MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPause() {
        super.onPause()
        if (this::searchItem.isInitialized) searchItem.collapseActionView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.filesList.adapter = BrowserAdapter(this)
        val path = arguments?.getString("path")
        activity?.title = path?.getNameFromPath() ?: "root"
        directoryModel.dataResult.observe(this, Observer<Directory> { update(it) })
        directoryModel.exception.observe(this, Observer { onError(it?.getContent()) })
        showProgress()
        if (path != null) binding.ariane.run {
            visibility = View.VISIBLE
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL).apply {
                setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_keyboard_arrow_right_indigo_700_18dp)!!)
            })
            val pathAdapter = PathAdapter(this@Browser, path)
            adapter = pathAdapter
            scrollToPosition(pathAdapter.itemCount-1)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.browser, menu)
        searchItem = menu.findItem(R.id.ml_menu_filter)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = getString(R.string.edit_hint_search)
        searchView.setOnQueryTextListener(directoryModel)
        searchItem.setOnActionExpandListener(directoryModel)
    }

    private fun update(directory: Directory?) {
        if (directory === null) return
        showProgress(false)
        @Suppress("UNCHECKED_CAST")
        (binding.filesList.adapter as BrowserAdapter).update(directory.files.sortedBy { !it.isDirectory })
    }

    @ExperimentalCoroutinesApi
    override fun open(file: File) {
        if (file.isDirectory)
            activity?.replaceFragment(R.id.fragment_placeholder, Browser().putStringExtra("path", file.path), file.path.getNameFromPath(), true)
        else
            LinkCreatorDialog().putStringExtra("path", file.path).show(activity?.supportFragmentManager, "linkin park")
    }

    override fun onRefresh() {
        directoryModel.refresh()
    }
}
