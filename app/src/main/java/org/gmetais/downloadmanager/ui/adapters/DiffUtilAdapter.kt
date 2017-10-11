package org.gmetais.downloadmanager.ui.adapters

import android.support.annotation.MainThread
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.util.*


abstract class DiffUtilAdapter<D, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    protected var mDataset: List<D> = listOf()
    private val mPendingUpdates = ArrayDeque<List<D>>()
    private val diffCallback by lazy(LazyThreadSafetyMode.NONE) { DiffCallback() }

    @MainThread
    fun update (list: List<D>) {
        mPendingUpdates.add(list)
        if (mPendingUpdates.size == 1)
            internalUpdate(list)
    }

    private fun internalUpdate(list: List<D>) = launch(UI) {
        val calculationJob = async { DiffUtil.calculateDiff(diffCallback.apply { newList = list.toList() }, false) }
        val result = calculationJob.await()
        if (!calculationJob.isCompletedExceptionally) {
            mDataset = diffCallback.newList
            result.dispatchUpdatesTo(this@DiffUtilAdapter)
        }
        processQueue()
    }

    private fun processQueue() {
        mPendingUpdates.remove()
        if (!mPendingUpdates.isEmpty())
            internalUpdate(mPendingUpdates.peek())
    }

    private inner class DiffCallback : DiffUtil.Callback() {
        lateinit var newList: List<D>

        override fun getOldListSize() = mDataset.size

        override fun getNewListSize() = newList.size

        override fun areContentsTheSame(oldItemPosition : Int, newItemPosition : Int) = true

        override fun areItemsTheSame(oldItemPosition : Int, newItemPosition : Int) = mDataset[oldItemPosition] == newList[newItemPosition]
    }
}