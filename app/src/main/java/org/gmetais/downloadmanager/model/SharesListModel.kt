package org.gmetais.downloadmanager.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.experimental.launch
import org.gmetais.downloadmanager.data.SharedFile
import org.gmetais.downloadmanager.data.SharesDatabase
import org.gmetais.downloadmanager.repo.DatabaseRepo

@Suppress("EXPERIMENTAL_FEATURE_WARNING", "UNCHECKED_CAST")
class SharesListModel : ViewModel() {

    val exception : MutableLiveData<Exception?> by lazy { MutableLiveData<Exception?>().apply { value = null } }

    val dataResult: LiveData<List<SharedFile>> by lazy {
        refresh()
        SharesDatabase.db.sharesDao().getShares()
    }

    fun refresh() = launch {
        exception.postValue(DatabaseRepo.fetchShares())
    }

    fun delete(share: SharedFile) = launch {
        exception.postValue(DatabaseRepo.delete(share))
    }

    fun add(share: SharedFile) = launch { SharesDatabase.db.sharesDao().insertShares(share) }
}