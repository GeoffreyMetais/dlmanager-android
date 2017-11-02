package org.gmetais.downloadmanager.repo

import android.arch.persistence.room.Room
import android.support.annotation.MainThread
import kotlinx.coroutines.experimental.async
import org.gmetais.downloadmanager.Application
import org.gmetais.downloadmanager.data.SharedFile
import org.gmetais.downloadmanager.data.SharesDatabase

@Suppress("UNCHECKED_CAST")
object DatabaseRepo {
    val dao by lazy { Room.databaseBuilder(Application.context, SharesDatabase::class.java, "shares").build().sharesDao() }

    @MainThread
    suspend fun fetchShares(): Exception? {
        val result = ApiRepo.fetchShares()
        return when (result) {
            is List<*> -> asyncDbJob { dao.insertShares(result as List<SharedFile>) }
            is Exception -> result
            else -> Exception("Unknown error")
        }
    }

    @MainThread
    suspend fun delete(share: SharedFile): Exception? {
        val result = ApiRepo.delete(share.name)
        if (result === null)
            return asyncDbJob { dao.deleteShares(share) }
        return result as Exception?
    }

    @MainThread
    suspend fun add(share: SharedFile) : Any? {
        val result = ApiRepo.add(share)
        return when(result) {
            is SharedFile -> asyncDbJob { dao.insertShares(result) } ?: result
            is Exception -> result
            else -> Exception("Unknown error")
        }
    }

    private suspend inline fun asyncDbJob(crossinline dbCall: () -> Unit) : Exception? {
        val job = async { dbCall.invoke() }
        job.await()
        return job.getCompletionExceptionOrNull() as? Exception?
    }
}