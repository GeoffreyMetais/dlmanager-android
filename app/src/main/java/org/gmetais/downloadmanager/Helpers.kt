package org.gmetais.downloadmanager

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.View
import kotlinx.coroutines.experimental.delay
import org.gmetais.downloadmanager.data.SharedFile
import java.io.IOException

fun  String.getNameFromPath(): String {
    if (!this.endsWith('/')) return this.substringAfterLast('/')
    return this.substringBeforeLast('/').substringAfterLast('/')
}

fun <T : View> Activity.bind(@IdRes res : Int) : Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(res) }
}

fun <T : Fragment> T.putStringExtra(key: String, value: String) : T {
    if (this.arguments == null) arguments = Bundle()
    arguments!!.putString(key, value)
    return this
}

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) = beginTransaction().func().commit()

fun FragmentActivity.addFragment(frameId: Int, fragment: Fragment, tag: String) = supportFragmentManager.inTransaction { add(frameId, fragment, tag) }

fun FragmentActivity.addFragment(fragment: Fragment, tag: String) = supportFragmentManager.inTransaction { add(fragment, tag) }

fun FragmentActivity.removeFragment(fragment: Fragment) = supportFragmentManager.inTransaction { remove(fragment) }

fun FragmentActivity.removeFragment(tag: String) = supportFragmentManager.inTransaction { remove(supportFragmentManager.findFragmentByTag(tag)) }

fun FragmentActivity.replaceFragment(frameId: Int, fragment: Fragment, tag: String, backstack : Boolean = false) = supportFragmentManager.inTransaction {
    if (backstack) addToBackStack(tag)
    replace(frameId, fragment, tag)
}

fun FragmentActivity.getFragment(@IdRes id: Int): Fragment? = this.supportFragmentManager.findFragmentById(id)
fun FragmentActivity.getRootView()= (this.getFragment(R.id.fragment_placeholder)?.view ?: this.window.decorView)!!

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