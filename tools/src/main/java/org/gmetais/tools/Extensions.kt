package org.gmetais.tools

import android.view.View
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.SendChannel

@Suppress("FunctionName")
fun IoScope() = CoroutineScope(Dispatchers.IO + SupervisorJob())

fun <E> SendChannel<E>.safeOffer(value: E) = !isClosedForSend && try {
    offer(value)
} catch (e: CancellationException) {
    false
}

val View.lifecycleOwner
    get() = (context as LifecycleOwner)

val View.lifecycleScope
    get() = lifecycleOwner.lifecycleScope