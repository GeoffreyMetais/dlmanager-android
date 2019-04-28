package org.gmetais.tools

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Suppress("FunctionName")
fun IoScope() = CoroutineScope(Dispatchers.IO + SupervisorJob())
