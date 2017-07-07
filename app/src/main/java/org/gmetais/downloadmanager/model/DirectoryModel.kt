package org.gmetais.downloadmanager.model

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.gmetais.downloadmanager.Directory
import org.gmetais.downloadmanager.RequestManager

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
class DirectoryModel(val path: String?) : ViewModel() {

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

    class Factory(val path: String?) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return DirectoryModel(path) as T
        }
    }
}