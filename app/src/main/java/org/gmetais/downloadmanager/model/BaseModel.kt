package org.gmetais.downloadmanager.model

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
abstract class BaseModel<out T> : ViewModel() {

    val exception by lazy { MutableLiveData<Exception?>() }
    val dataResult: T by lazy { initData() }

    abstract fun refresh() : Job
    abstract fun initData(): T

    protected fun execute(call: suspend () -> Unit): Job = launch(UI, CoroutineStart.UNDISPATCHED) {
        try {
            call.invoke()
        } catch (e: Exception) {
            exception.value = e
        }
    }
}