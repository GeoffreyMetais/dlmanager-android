package org.gmetais.downloadmanager.ui

import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.widget.Filterable
import org.gmetais.downloadmanager.R
import java.lang.ref.WeakReference

class FilterDelegate(private val filterable: WeakReference<Filterable>, menu: Menu) : SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    private val searchItem = menu.findItem(R.id.ml_menu_filter)!!
    private val searchView = searchItem.actionView as SearchView

    init {
        searchView.queryHint = "search…"
        searchView.setOnQueryTextListener(this)
        searchItem.setOnActionExpandListener(this)
    }

    override fun onQueryTextSubmit(p0: String?) = false

    override fun onQueryTextChange(filterQueryString: String): Boolean {
        if (filterQueryString.length < 3)
            return false
        filterable.get()?.filter?.filter(filterQueryString)
        return true
    }

    override fun onMenuItemActionExpand(p0: MenuItem?) = true

    override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
        filterable.get()?.filter?.filter(null)
        return true
    }

    fun close() {
        searchItem.collapseActionView()
    }
}