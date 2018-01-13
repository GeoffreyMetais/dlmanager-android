package org.gmetais.downloadmanager.model

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.support.annotation.MainThread
import kotlinx.coroutines.experimental.CoroutineStart
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.gmetais.downloadmanager.data.SharedFile
import org.gmetais.downloadmanager.repo.DatabaseRepo

@Suppress("EXPERIMENTAL_FEATURE_WARNING", "UNCHECKED_CAST")
class SharesListModel : ViewModel() {

    val exception by lazy { MutableLiveData<Exception?>() }

    val dataResult by lazy {
        refresh()
        DatabaseRepo.dao.getShares()
    }

    fun refresh() = launch(UI) { exception.value = DatabaseRepo.fetchShares() }

    @MainThread
    fun delete(share: SharedFile) = launch(UI, CoroutineStart.UNDISPATCHED) { exception.value = DatabaseRepo.delete(share) }
}