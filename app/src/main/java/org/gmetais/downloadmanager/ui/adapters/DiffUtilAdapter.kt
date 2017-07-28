package org.gmetais.downloadmanager.ui.adapters

import android.support.annotation.MainThread
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import java.util.*


abstract class DiffUtilAdapter<D, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    protected var mDataset: List<D> = listOf()
    private val mPendingUpdates = ArrayDeque<List<D>>()

    @MainThread
    fun update (list: List<D>) {
        mPendingUpdates.add(list)
        if (mPendingUpdates.size == 1)
            internalUpdate(list)
    }

    @Suppress("EXPERIMENTAL_FEATURE_WARNING")
    private fun internalUpdate(list: List<D>) {
        launch(CommonPool) {
            val finalList = list.toList()
            val result = DiffUtil.calculateDiff(DiffCallback(finalList), false)
            launch(UI) {
                mDataset = finalList
                result.dispatchUpdatesTo(this@DiffUtilAdapter)
                processQueue()
            }
        }
    }

    private fun processQueue() {
        mPendingUpdates.remove()
        if (!mPendingUpdates.isEmpty())
            internalUpdate(mPendingUpdates.peek())
    }

    inner class DiffCallback(val newList: List<D>) : DiffUtil.Callback() {

        override fun getOldListSize() = mDataset.size

        override fun getNewListSize() = newList.size

        override fun areContentsTheSame(oldItemPosition : Int, newItemPosition : Int) = true

        override fun areItemsTheSame(oldItemPosition : Int, newItemPosition : Int) = mDataset[oldItemPosition] == newList[newItemPosition]
    }
}