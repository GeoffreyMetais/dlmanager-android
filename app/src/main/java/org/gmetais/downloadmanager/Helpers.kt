package org.gmetais.downloadmanager

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import android.view.View
import kotlinx.coroutines.delay
import org.gmetais.downloadmanager.data.SharedFile
import java.io.IOException

fun  String.getNameFromPath(): String {
    if (!this.endsWith('/')) return this.substringAfterLast('/')
    return this.substringBeforeLast('/').substringAfterLast('/')
}

fun <T : View> Activity.bind(@IdRes res : Int) : Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(res) }
}

fun <T : androidx.fragment.app.Fragment> T.putStringExtra(key: String, value: String) : T {
    if (this.arguments == null) arguments = Bundle()
    arguments!!.putString(key, value)
    return this
}

inline fun androidx.fragment.app.FragmentManager.inTransaction(func: androidx.fragment.app.FragmentTransaction.() -> androidx.fragment.app.FragmentTransaction) = beginTransaction().func().commit()

fun androidx.fragment.app.FragmentActivity.addFragment(frameId: Int, fragment: androidx.fragment.app.Fragment, tag: String) = supportFragmentManager.inTransaction { add(frameId, fragment, tag) }

fun androidx.fragment.app.FragmentActivity.addFragment(fragment: androidx.fragment.app.Fragment, tag: String) = supportFragmentManager.inTransaction { add(fragment, tag) }

fun androidx.fragment.app.FragmentActivity.removeFragment(fragment: androidx.fragment.app.Fragment) = supportFragmentManager.inTransaction { remove(fragment) }

fun androidx.fragment.app.FragmentActivity.removeFragment(tag: String) = supportFragmentManager.inTransaction { remove(supportFragmentManager.findFragmentByTag(tag)!!) }

fun androidx.fragment.app.FragmentActivity.replaceFragment(frameId: Int, fragment: androidx.fragment.app.Fragment, tag: String, backstack : Boolean = false) = supportFragmentManager.inTransaction {
    if (backstack) addToBackStack(tag)
    replace(frameId, fragment, tag)
}

fun androidx.fragment.app.Fragment.goTo(tag: String) = activity?.supportFragmentManager?.popBackStack(tag, 0)

fun androidx.fragment.app.FragmentActivity.getFragment(@IdRes id: Int): androidx.fragment.app.Fragment? = supportFragmentManager.findFragmentById(id)
fun androidx.fragment.app.FragmentActivity.getFragment(tag: String): androidx.fragment.app.Fragment? = supportFragmentManager.findFragmentByTag(tag)
fun androidx.fragment.app.FragmentActivity.getRootView()= (this.getFragment(R.id.fragment_placeholder)?.view ?: this.window.decorView)!!

fun Activity.share(share: SharedFile) = startActivity(Intent(Intent.ACTION_SEND)
        .putExtra(Intent.EXTRA_TEXT, share.link)
        .setType("text/plain"))

suspend fun <T> retry (
        times: Int = Int.MAX_VALUE,
        initialDelay: Long = 100L,
        maxDelay: Long = 10000L,
        factor: Long = 2L,
        block: suspend () -> T): T
{
    var currentDelay = initialDelay
    repeat(times - 1) {
        try {
            return block()
        } catch (e: IOException) {
            // you can log an error here and/or make a more finer-grained
            // analysis of the cause to see if retry is needed
        }
        delay(currentDelay)
        currentDelay = (currentDelay * factor).coerceAtMost(maxDelay)
    }
    return block() // last attempt
}