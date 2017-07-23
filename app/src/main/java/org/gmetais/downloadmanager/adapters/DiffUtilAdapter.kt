package org.gmetais.downloadmanager.adapters

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
            val result = DiffUtil.calculateDiff(DiffCallback(list), false)
            launch(UI) {
                mDataset = list
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