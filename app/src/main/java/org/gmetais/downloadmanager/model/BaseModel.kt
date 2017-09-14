package org.gmetais.downloadmanager.model

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.gmetais.downloadmanager.data.Result

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
abstract class BaseModel : ViewModel() {
    @Suppress("EXPERIMENTAL_FEATURE_WARNING")
    abstract suspend fun call(): Result

    val dataResult: MutableLiveData<Result> by lazy {
        refresh()
        MutableLiveData<Result>()
    }

    fun refresh() = launch(UI) { dataResult.value = call() }
}