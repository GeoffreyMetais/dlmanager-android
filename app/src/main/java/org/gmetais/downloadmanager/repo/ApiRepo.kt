@file:Suppress("EXPERIMENTAL_FEATURE_WARNING")

package org.gmetais.downloadmanager.repo

import org.gmetais.downloadmanager.data.RequestManager
import org.gmetais.downloadmanager.data.SharedFile
import org.gmetais.downloadmanager.model.BaseModel
import retrofit2.Response

object ApiRepo {

    suspend fun browse(path: String?) = retrofitResponseCall { RequestManager.browse(path) }

    suspend fun listShares() = retrofitResponseCall { RequestManager.listShares() }

    suspend fun add(file: SharedFile) = retrofitBooleanCall { RequestManager.add(file) }

    suspend fun delete(key: String) = retrofitBooleanCall { RequestManager.delete(key) }

    private inline fun <T> retrofitResponseCall(call: () -> Response<T>) : BaseModel.Result {
        try {
            with (call()) {
                if (isSuccessful)
                    return BaseModel.Result.Success(body()!!)
                else
                    return BaseModel.Result.Error(code(), message())
            }
        } catch(e: Exception) {
            return BaseModel.Result.Error(code = 408, message = e.localizedMessage)
        }
    }

    private inline fun retrofitBooleanCall(call: () -> Boolean) : Boolean {
        try {
            return call()
        } catch(e: Exception) {
            return false
        }

    }
}