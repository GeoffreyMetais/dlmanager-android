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
        var originalData : List<File>? = null

        private fun initData() : List<File> {
            @Suppress("UNCHECKED_CAST")
            if (originalData === null)
                originalData = ((dataResult.value as Success<*>).content as Directory).files
            return originalData!!
        }

        override fun performFiltering(charSequence: CharSequence?): FilterResults {
            val results = FilterResults()
            if (charSequence !== null) {
                val queryStrings = charSequence.trim().toString().toLowerCase().split(" ").filter { it.length > 2 }
                val list = ArrayList<File>()
                for (file in initData()) {
                    for (query in queryStrings)
                        if (file.path.contains(query, true)) {
                            list.add(file)
                            break
                        }
                }
                results.values = list
                results.count = list.size
            }
            return results
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults?) {
            val path = (dataResult.value as Success<Directory>).content.path
            if (filterResults?.values !== null && dataResult.value is Success<*>)
                dataResult.value = Success(Directory(path, filterResults.values as List<File>))
            else if (originalData !== null) {
                dataResult.value = Success(Directory(path, originalData!!))
                originalData = null
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