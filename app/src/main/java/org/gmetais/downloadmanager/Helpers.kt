package org.gmetais.downloadmanager

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.View

fun  String.getNameFromPath(): String {
    if (!this.endsWith('/'))
        return this.substringAfterLast('/')
    return this.substringBeforeLast('/').substringAfterLast('/')
}

fun <T : View> Activity.bind(@IdRes res : Int) : Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(res) }
}

fun Fragment.putStringExtra(key: String, value: String) : Fragment {
    if (this.arguments == null)
        arguments = Bundle()
    arguments.putString(key, value)
    return this
}

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) = beginTransaction().func().commit()

fun FragmentActivity.addFragment(frameId: Int, fragment: Fragment, tag: String) = supportFragmentManager.inTransaction { add(frameId, fragment, tag) }

fun FragmentActivity.addFragment(fragment: Fragment, tag: String) = supportFragmentManager.inTransaction { add(fragment, tag) }

fun FragmentActivity.removeFragment(fragment: Fragment) = supportFragmentManager.inTransaction { remove(fragment) }

fun FragmentActivity.removeFragment(tag: String) = supportFragmentManager.inTransaction { remove(supportFragmentManager.findFragmentByTag(tag)) }

fun FragmentActivity.replaceFragment(frameId: Int, fragment: Fragment, tag: String) = supportFragmentManager.inTransaction { replace(frameId, fragment, tag) }
