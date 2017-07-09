package org.gmetais.downloadmanager.model

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import org.gmetais.downloadmanager.Directory
import org.gmetais.downloadmanager.RequestManager
import retrofit2.Response

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
class DirectoryModel(val path: String?) : BaseModel<Directory>() {

    suspend override fun call(): Response<out Directory> = RequestManager.browse(path)

    class Factory(val path: String?) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return DirectoryModel(path) as T
        }
    }
}