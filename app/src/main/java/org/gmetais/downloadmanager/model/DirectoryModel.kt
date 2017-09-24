package org.gmetais.downloadmanager.model

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.v7.widget.SearchView
import android.view.MenuItem
import android.widget.Filter
import org.gmetais.downloadmanager.data.Directory
import org.gmetais.downloadmanager.data.File
import org.gmetais.downloadmanager.data.Success
import org.gmetais.downloadmanager.repo.ApiRepo

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
class DirectoryModel(val path: String?) : BaseModel(), SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    private val filter by lazy { FileFilter() }

    override suspend fun call() = ApiRepo.browse(path)

    class Factory(val path: String?) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return DirectoryModel(path) as T
        }
    }

    inner class FileFilter : Filter() {
        private var originalData : Directory? = null

        private fun initData() : Directory? {
            @Suppress("UNCHECKED_CAST")
            if (originalData === null)
                originalData = (dataResult.value as? Success<Directory>)?.content
            return originalData
        }

        override fun performFiltering(charSequence: CharSequence?) = FilterResults().apply {
            charSequence?.let {
                val queryStrings = it.trim().toString().toLowerCase().split(" ").filter { it.length > 2 }
                val list = ArrayList<File>()
                initData()?.let {
                    for (file in it.files) {
                        for (query in queryStrings)
                            if (file.path.contains(query, true)) {
                                list.add(file)
                                break
                            }
                    }
                }
                values = list
                count = list.size
            }
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults?) {
            originalData?.let {
                if (filterResults?.values !== null)
                    dataResult.value = Success(it.copy(files = filterResults.values as List<File>))
                else {
                    dataResult.value = Success(it)
                    originalData = null
                }
            }
        }
    }

    override fun onQueryTextSubmit(p0: String?) = false

    override fun onQueryTextChange(filterQueryString: String): Boolean {
        if (filterQueryString.length < 3)
            return false
        filter.filter(filterQueryString)
        return true
    }

    override fun onMenuItemActionExpand(p0: MenuItem?) = true

    override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
        filter.filter(null)
        return true
    }
}