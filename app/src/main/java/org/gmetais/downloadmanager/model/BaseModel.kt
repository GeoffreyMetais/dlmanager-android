package org.gmetais.downloadmanager.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import org.gmetais.tools.Event

@ExperimentalCoroutinesApi
@Suppress("EXPERIMENTAL_FEATURE_WARNING")
abstract class BaseModel<T> : ViewModel() {

    val exception = MutableLiveData<Event<Exception>>()
    open val dataResult : LiveData<T> = MutableLiveData()
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable -> exception.value = Event(throwable as? Exception ?: Exception(throwable)) }

    abstract fun refresh() : Job

    init {
        viewModelScope.launch { refresh() }
    }

    protected fun execute(call: suspend () -> Unit): Job = viewModelScope.launch(exceptionHandler, CoroutineStart.UNDISPATCHED) { call() }
}