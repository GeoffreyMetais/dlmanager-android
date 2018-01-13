package org.gmetais.downloadmanager.model

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.gmetais.downloadmanager.data.Error
import org.gmetais.downloadmanager.data.Result
import org.gmetais.downloadmanager.data.Success

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
abstract class BaseModel<T> : ViewModel() {

    abstract suspend fun call(): Result

    val exception by lazy { MutableLiveData<String?>() }

    val dataResult: MutableLiveData<T> by lazy {
        refresh()
        MutableLiveData<T>()
    }

    fun refresh() = launch(UI) {
        call().let {
            when (it) {
                is Success<*> -> dataResult.value = it.content as T
                is Error -> exception.value = it.message
            }
        }
    }
}