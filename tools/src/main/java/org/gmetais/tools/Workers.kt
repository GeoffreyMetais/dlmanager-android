package org.gmetais.tools

import kotlinx.coroutines.experimental.asCoroutineDispatcher
import java.util.concurrent.Executors

val IO = Executors.newSingleThreadExecutor({ runnable ->
    Thread(runnable, "io").apply { priority = Thread.NORM_PRIORITY + 1 }
}).asCoroutineDispatcher()