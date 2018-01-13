package org.gmetais.downloadmanager.model

import android.widget.Filter
import org.gmetais.downloadmanager.data.Directory
import org.gmetais.downloadmanager.data.File

class FileFilter(private val directoryModel: DirectoryModel) : Filter() {
    private var originalData : Directory? = null

    private fun initData() : Directory? {
        @Suppress("UNCHECKED_CAST")
        if (originalData === null) originalData = (directoryModel.dataResult.value)
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
                directoryModel.dataResult.value = it.copy(files = filterResults.values as List<File>)
            else {
                directoryModel.dataResult.value = it
                originalData = null
            }
        }
    }
}