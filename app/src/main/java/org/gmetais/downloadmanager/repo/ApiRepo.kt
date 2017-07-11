@file:Suppress("EXPERIMENTAL_FEATURE_WARNING")

package org.gmetais.downloadmanager.repo

import org.gmetais.downloadmanager.data.RequestManager
import org.gmetais.downloadmanager.data.SharedFile
import retrofit2.Response


object ApiRepo {

    sealed class Result {
        data class Success<out T>(val content: T) : Result()
        data class Error(val code: Int, val message: String) : Result()
    }

    suspend fun browse(path: String?) = retrofitCall { RequestManager.browse(path) }

    suspend fun listShares() = retrofitCall { RequestManager.listShares() }

    suspend fun add(file: SharedFile) = RequestManager.add(file)

    suspend fun delete(key: String) = RequestManager.delete(key)

    private inline fun <T> retrofitCall(call: () -> Response<T>) : Result {
        try {
            with (call()) {
                if (isSuccessful)
                    return Result.Success(body()!!)
                else
                    return Result.Error(code(), errorBody()?.source()?.readUtf8() ?: message())
            }
        } catch(e: Exception) {
            return Result.Error(code = 408, message = e.localizedMessage)
        }
    }
}