@file:Suppress("EXPERIMENTAL_FEATURE_WARNING")

package org.gmetais.downloadmanager.repo

import kotlinx.coroutines.experimental.suspendCancellableCoroutine
import org.gmetais.downloadmanager.data.RequestManager
import org.gmetais.downloadmanager.data.SharedFile
import org.gmetais.downloadmanager.model.BaseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ApiRepo {

    suspend fun browse(path: String?) = retrofitResponseCall { RequestManager.browse(path) }

    suspend fun listShares() = retrofitResponseCall { RequestManager.listShares() }

    suspend fun add(file: SharedFile) = retrofitBooleanCall { RequestManager.add(file) }

    suspend fun delete(key: String) = retrofitBooleanCall { RequestManager.delete(key) }


    private suspend inline fun <reified T> retrofitResponseCall(crossinline call: () -> Call<T>) : BaseModel.Result {
        try {
            with(retrofitSuspendCall(call)) {
                if (isSuccessful)
                    return BaseModel.Result.Success(body()!!)
                else
                    return BaseModel.Result.Error(code(), message())
            }
        } catch(e: Exception) {
            return BaseModel.Result.Error(408, e.localizedMessage)
        }

    }

    private suspend inline fun retrofitBooleanCall(crossinline call: () -> Call<Void>) : Boolean {
        try {
            return retrofitSuspendCall(call).isSuccessful
        } catch(e: Exception) {
            return false
        }
    }

    private suspend inline fun <reified T> retrofitSuspendCall(crossinline call: () -> Call<T>) : Response<T> {
        return suspendCancellableCoroutine { continuation ->
            with(call.invoke()) {
                enqueue(object : Callback<T> {
                    override fun onResponse(call: Call<T>?, response: Response<T>) {
                        continuation.resume(response)
                    }
                    override fun onFailure(call: Call<T>, t: Throwable) {
                        // Don't bother with resuming the continuation if it is already cancelled.
                        if (continuation.isCancelled) return
                        continuation.resumeWithException(t)
                    }
                })
                continuation.invokeOnCompletion {
                    if (continuation.isCancelled)
                        try {
                            cancel()
                        } catch (ex: Throwable) {}
                }
            }
        }
    }
}