package org.gmetais.downloadmanager.model

import kotlinx.coroutines.experimental.CoroutineStart
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.gmetais.downloadmanager.data.Directory
import org.gmetais.downloadmanager.data.File

class FileFilter(private val directoryModel: DirectoryModel) {
    private var originalData : Directory? = null

    private fun initData() : Directory? {
        if (originalData === null) originalData = (directoryModel.dataResult.value)
        return originalData
    }

    fun filter(charSequence: CharSequence?) = launch(UI, CoroutineStart.UNDISPATCHED) {
        publish(filteringJob(charSequence).await())
    }

    private fun filteringJob(charSequence: CharSequence?) = async {
        if (charSequence !== null) initData()?.let {
            val list = mutableListOf<File>()
            val queryStrings = charSequence.trim().toString().toLowerCase().split(" ").filter { it.length > 2 }
            for (file in it.files) {
                val filename = file.path.substringAfterLast('/')
                for (query in queryStrings)
                    if (filename.contains(query, true)) {
                        list.add(file)
                        break
                    }
            }
            return@async list
        }
        return@async listOf<File>()
    }

    private fun publish(list: List<File>?) {
        originalData?.let {
            if (list?.isEmpty() == false)
                directoryModel.dataResult.value = it.copy(files = list)
            else {
                directoryModel.dataResult.value = it
                originalData = null
            }
        }
    }
}