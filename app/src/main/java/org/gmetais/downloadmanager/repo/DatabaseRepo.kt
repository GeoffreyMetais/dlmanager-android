package org.gmetais.downloadmanager.repo

import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import kotlinx.coroutines.experimental.launch
import org.gmetais.downloadmanager.data.SharedFile
import org.gmetais.downloadmanager.data.SharesDatabase


@Suppress("UNCHECKED_CAST")
object DatabaseRepo {

    @WorkerThread
    suspend fun fetchShares(): Exception? {
        val result = ApiRepo.fetchShares()
        return when (result) {
            is List<*> -> {
                SharesDatabase.db.sharesDao().insertShares(result as List<SharedFile>)
                null
            }
            is Exception -> result
            else -> Exception("Unknown error")
        }
    }

    @WorkerThread
    suspend fun delete(share: SharedFile): Exception? {
        val result = ApiRepo.delete(share.name)
        if (result === null)
            SharesDatabase.db.sharesDao().deleteShares(share)
        return result as Exception?
    }

    @MainThread
    suspend fun add(share: SharedFile) : Any? {
        val result = ApiRepo.add(share)
        return when(result) {
            is SharedFile -> {
                launch { SharesDatabase.db.sharesDao().insertShares(result) }
                result
            }
            is Exception -> result
            else -> Exception("Unknown error")
        }
    }
}