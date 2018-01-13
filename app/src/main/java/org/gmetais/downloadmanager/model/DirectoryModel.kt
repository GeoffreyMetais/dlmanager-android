package org.gmetais.downloadmanager.model

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.v7.widget.SearchView
import android.view.MenuItem
import org.gmetais.downloadmanager.data.Directory
import org.gmetais.downloadmanager.repo.ApiRepo

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
class DirectoryModel(val path: String?) : BaseModel<Directory>(), SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    private val filter by lazy { FileFilter(this) }

    override suspend fun call() = ApiRepo.browse(path)

    class Factory(val path: String?) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return DirectoryModel(path) as T
        }
    }

    //FIlter listeners
    override fun onQueryTextSubmit(p0: String?) = false

    override fun onQueryTextChange(filterQueryString: String) = if (filterQueryString.length < 3)
        false
    else {
        filter.filter(filterQueryString)
        true
    }

    override fun onMenuItemActionExpand(p0: MenuItem?) = true

    override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
        filter.filter(null)
        return true
    }
}