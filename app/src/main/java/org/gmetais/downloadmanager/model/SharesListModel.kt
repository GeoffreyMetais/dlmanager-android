package org.gmetais.downloadmanager.model

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.gmetais.downloadmanager.data.SharedFile
import org.gmetais.downloadmanager.repo.ApiRepo

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
class SharesListModel : BaseModel() {

    override suspend fun call() = ApiRepo.listShares()

    @Suppress("UNCHECKED_CAST")
    fun delete(share: SharedFile) {
        async(CommonPool) {
            if (ApiRepo.delete(share.name)) {
                (dataResult.value as? BaseModel.Result.Success<MutableList<SharedFile>>)?.let {
                    it.content.remove(share)
                    dataResult.postValue(it)
                }
            }
        }
    }
}