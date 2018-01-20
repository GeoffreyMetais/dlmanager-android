package org.gmetais.downloadmanager.model

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.experimental.CoroutineExceptionHandler
import kotlinx.coroutines.experimental.CoroutineStart
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
abstract class BaseModel<out T> : ViewModel() {

    val exception by lazy { MutableLiveData<Exception?>() }
    val dataResult: T by lazy { initData() }
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable -> exception.value = throwable as? Exception ?: Exception(throwable) }

    abstract fun refresh() : Job
    abstract fun initData(): T

    protected fun execute(call: suspend () -> Unit): Job = launch(UI+exceptionHandler, CoroutineStart.UNDISPATCHED) { call.invoke() }
}