@file:Suppress("EXPERIMENTAL_FEATURE_WARNING")

package org.gmetais.downloadmanager.repo

import org.gmetais.downloadmanager.data.RequestManager
import org.gmetais.downloadmanager.data.SharedFile
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.experimental.suspendCoroutine

object ApiRepo {

    suspend fun browse(path: String?) = retrofitResponseCall { RequestManager.browse(path) }

    suspend fun fetchShares() = retrofitResponseCall { RequestManager.listShares() }

    suspend fun add(file: SharedFile) = retrofitResponseCall { RequestManager.add(file) }

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
}