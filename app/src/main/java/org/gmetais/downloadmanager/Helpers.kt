package org.gmetais.downloadmanager

import android.app.Activity
import android.support.annotation.IdRes
import android.view.View

fun  String.getNameFromPath(): String {
    var trailing = this.endsWith('/')
    var index = if (!trailing) this.lastIndexOf('/') else this.substring(0, this.length-2).lastIndexOf('/')
    return this.substring(index+1, if (trailing) this.length-1 else this.length)
}

fun <T : View> Activity.bind(@IdRes res : Int) : Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(res) }
}

fun <T : View> View.bind(@IdRes res : Int) : Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(res) }
}