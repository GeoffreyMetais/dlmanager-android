package org.gmetais.downloadmanager.model

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.experimental.CoroutineStart
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
abstract class BaseModel<T> : ViewModel() {

    abstract suspend fun update(): T

    val exception by lazy { MutableLiveData<String?>() }

    val dataResult: MutableLiveData<T> by lazy {
        launch(UI) { refresh() }
        MutableLiveData<T>()
    }

    fun refresh() = launch(UI, CoroutineStart.UNDISPATCHED) {
        try {
            dataResult.value = update()
        } catch (e: Exception) {
            exception.value = e.message
        }
    }
}