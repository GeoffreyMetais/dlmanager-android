package org.gmetais.downloadmanager.model

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import retrofit2.Response

abstract class BaseModel<out T> : ViewModel() {
    @Suppress("EXPERIMENTAL_FEATURE_WARNING")
    abstract suspend fun call(): Response<out T>

    val dataResult: MutableLiveData<Result> by lazy {
        loadData()
        MutableLiveData<Result>()
    }

    fun loadData() {
        async(CommonPool) {
            with(call()) {
                dataResult.postValue(if (isSuccessful) Result.Success(body()!!) else Result.Error(code(), errorBody()?.source()?.readUtf8() ?: message()))
            }
        }
    }

    sealed class Result {
        data class Success<out T>(val content: T) : Result()
        data class Error(val code: Int, val message: String) : Result()
    }
}