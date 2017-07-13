package org.gmetais.downloadmanager.model

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import org.gmetais.downloadmanager.data.Directory
import org.gmetais.downloadmanager.repo.ApiRepo

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
class DirectoryModel(val path: String?) : BaseModel() {

    override suspend fun call() = ApiRepo.browse(path)

    class Factory(val path: String?) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return DirectoryModel(path) as T
        }
    }
}