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
object DataRepo : CoroutineScope by IoScope() {
    val dao by lazy { Room.databaseBuilder(Application.context, SharesDatabase::class.java, "shares").build().sharesDao() }

    @MainThread
    suspend fun fetchShares() {
        val result = getShares()
        asyncDbJob { dao.insertShares(result as List<SharedFile>) }
    }

    @MainThread
    suspend fun delete(share: SharedFile) {
        delete(share.name)
        asyncDbJob { dao.deleteShares(share) }
    }

    @MainThread
    suspend fun add(share: SharedFile) : SharedFile {
        val result = addFile(share)
        if (isActive) asyncDbJob { dao.insertShares(result) }
        return result

    }

    private suspend inline fun asyncDbJob(crossinline dbCall: () -> Unit) {
        val job = launch { dbCall.invoke() }
        job.join()
    }
}

suspend fun browse(path: String?) = retrofitResponseCall { RequestManager.browse(path) }

suspend fun getShares() = retrofitResponseCall { RequestManager.listShares() }

suspend fun addFile(file: SharedFile) = retrofitResponseCall { RequestManager.add(file) }

suspend fun delete(key: String) = retrofitResponse { RequestManager.delete(key) }

private suspend inline fun retrofitResponse(crossinline call: () -> Call<Void>) {
    with(retrofitSuspendCall(call)) { if (!isSuccessful) throw Exception(message()) }
}

private suspend inline fun <reified T> retrofitResponseCall(crossinline call: () -> Call<T>) : T {
    with(retrofitSuspendCall(call)) {
        if (isSuccessful) return body()!!
        else throw Exception(message())
    }
}

private suspend inline fun <reified T> retrofitSuspendCall(crossinline call: () -> Call<T>) : Response<T> = suspendCoroutine { continuation ->
    call.invoke().enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>?, response: Response<T>) = continuation.resume(response)
        override fun onFailure(call: Call<T>, t: Throwable) = continuation.resumeWithException(t)
    })
}
