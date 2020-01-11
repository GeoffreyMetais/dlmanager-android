package org.gmetais.downloadmanager.model

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.gmetais.downloadmanager.data.SharedFile
import org.gmetais.downloadmanager.repo.DataRepo

@Suppress("EXPERIMENTAL_FEATURE_WARNING", "UNCHECKED_CAST")
class SharesListModel : BaseModel<List<SharedFile>>() {

    override val dataResult = DataRepo.dao.getShares()

    override fun refresh() = execute { DataRepo.fetchShares() }

    @MainThread
    fun delete(share: SharedFile) = execute { DataRepo.delete(share) }
}