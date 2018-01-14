package org.gmetais.downloadmanager.model

import android.arch.lifecycle.LiveData
import android.support.annotation.MainThread
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.gmetais.downloadmanager.data.SharedFile
import org.gmetais.downloadmanager.repo.DatabaseRepo

@Suppress("EXPERIMENTAL_FEATURE_WARNING", "UNCHECKED_CAST")
class SharesListModel : BaseModel<LiveData<List<SharedFile>>>() {

    override fun initData(): LiveData<List<SharedFile>> {
        launch(UI) { refresh() }
        return DatabaseRepo.dao.getShares()
    }

    override fun refresh() = execute { DatabaseRepo.fetchShares() }

    @MainThread
    fun delete(share: SharedFile) = execute { DatabaseRepo.delete(share) }
}