package org.gmetais.downloadmanager.repo

import androidx.annotation.MainThread
import androidx.room.Room
import kotlinx.coroutines.*
import org.gmetais.downloadmanager.Application
import org.gmetais.downloadmanager.data.RequestManager
import org.gmetais.downloadmanager.data.SharedFile
import org.gmetais.downloadmanager.data.SharesDatabase
import org.gmetais.tools.IoScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Suppress("UNCHECKED_CAST")
object DataRepo {
    val dao by lazy { Room.databaseBuilder(Application.context, SharesDatabase::class.java, "shares").build().sharesDao() }

    @MainThread
    suspend fun fetchShares() {
        val result = getShares()
        withContext(Dispatchers.IO) { dao.insertShares(result as List<SharedFile>) }
    }

    @MainThread
    suspend fun delete(share: SharedFile) {
        delete(share.name)
        withContext(Dispatchers.IO) { dao.deleteShares(share) }
    }

    @MainThread
    suspend fun add(share: SharedFile) : SharedFile {
        val result = addFile(share)
        withContext(Dispatchers.IO) { dao.insertShares(result) }
        return result

    }
}

suspend fun browse(path: String?) = RequestManager.browse(path)

suspend fun getShares() = RequestManager.listShares()

suspend fun addFile(file: SharedFile) = RequestManager.add(file)

suspend fun delete(key: String) = RequestManager.delete(key)
