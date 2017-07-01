package org.gmetais.downloadmanager.model

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.gmetais.downloadmanager.Directory
import org.gmetais.downloadmanager.RequestManager

class DirectoryModel : ViewModel() {
    var path : String? = null

    val directory: MutableLiveData<Directory> by lazy {
        loadDirectory()
        MutableLiveData<Directory>()
    }

    private fun loadDirectory() {
        async(CommonPool) {
            with (RequestManager.browse(path)) {
                if (isSuccessful)
                    directory.postValue(body())
            }
        }
    }
}