package org.gmetais.downloadmanager

import android.app.Activity
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.View

fun  String.getNameFromPath(): String {
    if (!this.endsWith('/'))
        return this.substringAfterLast('/')
    return this.substringBeforeLast('/').substringAfterLast('/')
}

fun <T : View> Activity.bind(@IdRes res : Int) : Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(res) }
}

fun <T : View> Fragment.bind(@IdRes res : Int) : Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) { view!!.findViewById<T>(res) }
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

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) = beginTransaction().func().commit()

fun AppCompatActivity.addFragment(frameId: Int, fragment: Fragment, tag: String) = supportFragmentManager.inTransaction { add(frameId, fragment, tag) }

fun AppCompatActivity.removeFragment(fragment: Fragment) = supportFragmentManager.inTransaction { remove(fragment) }

fun AppCompatActivity.removeFragment(tag: String) = supportFragmentManager.inTransaction { remove(supportFragmentManager.findFragmentByTag(tag)) }

fun AppCompatActivity.replaceFragment(frameId: Int, fragment: Fragment, tag: String) = supportFragmentManager.inTransaction { replace(frameId, fragment, tag) }