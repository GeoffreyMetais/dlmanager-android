package org.gmetais.downloadmanager.model

import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.launch
import org.gmetais.downloadmanager.data.Directory
import org.gmetais.downloadmanager.repo.browse

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
class DirectoryModel(val path: String?) : BaseModel<MutableLiveData<Directory>>(), SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    private val filter by lazy { FileFilter(this) }

    override fun initData(): MutableLiveData<Directory> {
        launch { refresh() }
        return MutableLiveData()
    }

    override fun refresh() = execute { dataResult.value = browse(path) }

    class Factory(val path: String?): ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return DirectoryModel(path) as T
        }
    }

    //Filter listeners

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