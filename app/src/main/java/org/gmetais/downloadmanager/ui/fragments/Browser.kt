package org.gmetais.downloadmanager.ui.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.gmetais.downloadmanager.R
import org.gmetais.downloadmanager.data.Directory
import org.gmetais.downloadmanager.getNameFromPath
import org.gmetais.downloadmanager.model.DirectoryModel
import org.gmetais.downloadmanager.putStringExtra
import org.gmetais.downloadmanager.replaceFragment
import org.gmetais.downloadmanager.ui.LinkCreatorDialog
import org.gmetais.downloadmanager.ui.adapters.BrowserAdapter
import org.gmetais.downloadmanager.ui.adapters.PathAdapter
import org.gmetais.tools.Click

class Browser : BaseBrowser() {

    private val directoryModel: DirectoryModel by activityViewModels { DirectoryModel.Factory(arguments?.getString("path")) }
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
        val browserAdapter = BrowserAdapter()
        binding.filesList.adapter = browserAdapter
        val path = arguments?.getString("path")
        activity?.title = path?.getNameFromPath() ?: "root"
        directoryModel.dataResult.observe(viewLifecycleOwner, Observer<Directory> {
            update(it)
        })
        directoryModel.exception.observe(viewLifecycleOwner, Observer { onError(it?.getContent()) })
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
        browserAdapter.events.onEach { it.process() }.launchIn(lifecycleScope)
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

    override fun onRefresh() {
        directoryModel.refresh()
    }

    private fun Click.process() {
        val item = directoryModel.dataResult.value?.files?.get(position) ?: return
        if (item.isDirectory)
            activity?.replaceFragment(R.id.fragment_placeholder, Browser().putStringExtra("path", item.path), item.path.getNameFromPath(), true)
        else
            activity?.supportFragmentManager?.let { LinkCreatorDialog().putStringExtra("path", item.path).show(it, "linkin park") }
    }
}
