package org.gmetais.downloadmanager.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.experimental.launch
import org.gmetais.downloadmanager.data.SharedFile
import org.gmetais.downloadmanager.data.SharesDatabase
import org.gmetais.downloadmanager.repo.ApiRepo

@Suppress("EXPERIMENTAL_FEATURE_WARNING", "UNCHECKED_CAST")
class SharesListModel : ViewModel() {

    val dataResult: LiveData<List<SharedFile>> by lazy {
        refresh()
        SharesDatabase.db.sharesDao().getShares()
    }

    fun refresh() = launch { ApiRepo.fetchShares() }

    fun delete(share: SharedFile) = launch {
        if (ApiRepo.delete(share.name))
            SharesDatabase.db.sharesDao().deleteShares(share)
    }

    fun add(share: SharedFile) = launch { SharesDatabase.db.sharesDao().insertShares(share) }
}