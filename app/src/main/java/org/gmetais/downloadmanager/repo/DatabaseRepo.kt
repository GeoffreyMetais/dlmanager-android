package org.gmetais.downloadmanager.repo

import androidx.annotation.MainThread
import androidx.room.Room
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.NonCancellable
import kotlinx.coroutines.experimental.launch
import org.gmetais.downloadmanager.Application
import org.gmetais.downloadmanager.data.SharedFile
import org.gmetais.downloadmanager.data.SharesDatabase

@Suppress("UNCHECKED_CAST")
object DatabaseRepo : CoroutineScope {
    override val coroutineContext = Dispatchers.IO
    val dao by lazy { Room.databaseBuilder(Application.context, SharesDatabase::class.java, "shares").build().sharesDao() }

    @MainThread
    suspend fun fetchShares() {
        val result = ApiRepo.fetchShares()
        asyncDbJob { dao.insertShares(result as List<SharedFile>) }
    }

    @MainThread
    suspend fun delete(share: SharedFile) {
        ApiRepo.delete(share.name)
        asyncDbJob { dao.deleteShares(share) }
    }

    @MainThread
    suspend fun add(share: SharedFile) : SharedFile {
        val result = ApiRepo.add(share)
        if (NonCancellable.isActive) asyncDbJob { dao.insertShares(result) }
        return result

    }

    private suspend inline fun asyncDbJob(crossinline dbCall: () -> Unit) {
        val job = launch { dbCall.invoke() }
        job.join()
        if (job.isCancelled) throw job.getCancellationException()
    }
}