package org.gmetais.downloadmanager.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import org.gmetais.tools.Event

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
abstract class BaseModel<out T> : ViewModel(), CoroutineScope {
    override val coroutineContext = Dispatchers.Main

    val exception by lazy { MutableLiveData<Event<Exception>>() }
    val dataResult: T by lazy { initData() }
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable -> exception.value = Event(throwable as? Exception ?: Exception(throwable)) }

    abstract fun refresh() : Job
    abstract fun initData(): T

    protected fun execute(call: suspend () -> Unit): Job = launch(exceptionHandler, CoroutineStart.UNDISPATCHED) { call.invoke() }
}