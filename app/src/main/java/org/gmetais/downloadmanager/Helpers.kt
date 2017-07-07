package org.gmetais.downloadmanager

import android.app.Activity
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.view.View

fun  String.getNameFromPath(): String {
    if (!this.endsWith('/'))
        return this.substringAfterLast('/')
    return this.substringBeforeLast('/').substringAfterLast('/')
}

fun <T : View> Activity.bind(@IdRes res : Int) : Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(res) }
}

fun <T : View> Fragment.bind(@IdRes res : Int) : Lazy<T?> {
    return lazy(LazyThreadSafetyMode.NONE) { view?.findViewById<T>(res) }
}

fun <T : View> View.bind(@IdRes res : Int) : Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(res) }
}

fun Fragment.putStringExtra(key: String, value: String) : Fragment {
    if (this.arguments == null)
        arguments = Bundle()
    arguments.putString(key, value)
    return this
}