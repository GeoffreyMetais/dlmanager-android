package org.gmetais.downloadmanager.ui.adapters

import android.widget.Filter
import android.widget.Filterable
import org.gmetais.downloadmanager.R
import org.gmetais.downloadmanager.data.File

class BrowserAdapter(handler: IHandler) : BaseAdapter<File>(handler), Filterable {

    private val filter by lazy { FileFilter() }

    interface IHandler {
        fun open(file : File)
    }

    override fun getLayout() = R.layout.browser_item

    override fun getFilter(): Filter = filter

    inner class FileFilter : Filter() {
        var originalData : List<File>? = null

        fun initData() : List<File> {
            if (originalData === null)
                originalData = mDataset.toList()
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

        override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults?) {
            if (filterResults !== null && filterResults.values !== null)
                update(filterResults.values as List<File>)
            else if (originalData !== null) {
                update(originalData as List<File>)
                originalData = null
            }
        }
    }
}
