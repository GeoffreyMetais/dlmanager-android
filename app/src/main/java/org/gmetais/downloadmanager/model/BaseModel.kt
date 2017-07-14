package org.gmetais.downloadmanager.model

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
abstract class BaseModel : ViewModel() {
    @Suppress("EXPERIMENTAL_FEATURE_WARNING")
    abstract suspend fun call(): Result

    val dataResult: MutableLiveData<Result> by lazy {
        loadData()
        MutableLiveData<Result>()
    }

    fun loadData() {
        async(CommonPool) { dataResult.postValue(call()) }
    }

    sealed class Result {
        data class Success<out T>(val content: T) : Result()
        data class Error(val code: Int, val message: String) : Result()
    }
}