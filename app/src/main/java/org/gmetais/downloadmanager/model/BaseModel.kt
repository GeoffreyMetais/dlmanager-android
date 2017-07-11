package org.gmetais.downloadmanager.model

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.gmetais.downloadmanager.repo.ApiRepo

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
abstract class BaseModel<out T> : ViewModel() {
    @Suppress("EXPERIMENTAL_FEATURE_WARNING")
    abstract suspend fun call(): ApiRepo.Result

    val dataResult: MutableLiveData<ApiRepo.Result> by lazy {
        loadData()
        MutableLiveData<ApiRepo.Result>()
    }

    fun loadData() {
        async(CommonPool) { dataResult.postValue(call()) }
    }
}