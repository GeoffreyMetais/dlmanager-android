package org.gmetais.tools

import androidx.lifecycle.GenericLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.*

fun LifecycleOwner.createJob(cancelEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY): Job = Job().also { job ->
    lifecycle.addObserver(object : GenericLifecycleObserver {
        override fun onStateChanged(source: LifecycleOwner?, event: Lifecycle.Event) {
            if (event == cancelEvent) {
                lifecycle.removeObserver(this)
                job.cancel()
            }
        }
    })
}

private val lifecycleCoroutineScopes = mutableMapOf<Lifecycle, CoroutineScope>()

@ExperimentalCoroutinesApi
val LifecycleOwner.coroutineScope: CoroutineScope
    get() = lifecycleCoroutineScopes[lifecycle] ?: createJob().let {
        val newScope = CoroutineScope(it + Dispatchers.Main.immediate)
        lifecycleCoroutineScopes[lifecycle] = newScope
        it.invokeOnCompletion { lifecycleCoroutineScopes -= lifecycle }
        newScope
    }

@Suppress("FunctionName")
fun IoScope() = CoroutineScope(Dispatchers.IO + SupervisorJob())
