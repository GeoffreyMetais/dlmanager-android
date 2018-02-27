package org.gmetais.downloadmanager.ui.adapters

import android.support.annotation.WorkerThread
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.actor
import kotlinx.coroutines.experimental.withContext

abstract class DiffUtilAdapter<D, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    protected var mDataset: List<D> = listOf()
    private val diffCallback by lazy(LazyThreadSafetyMode.NONE) { DiffCallback() }
    private val eventActor = actor<List<D>>(capacity = Channel.CONFLATED) { for (list in channel) internalUpdate(list) }

    fun update (list: List<D>) = eventActor.offer(list)

    @WorkerThread
    private suspend fun internalUpdate(list: List<D>) {
        val dataSet = list.toList()
        val result = DiffUtil.calculateDiff(diffCallback.apply { newList = dataSet }, false)
        withContext(UI) {
            mDataset = dataSet
            result.dispatchUpdatesTo(this@DiffUtilAdapter)
        }
    }

    private inner class DiffCallback : DiffUtil.Callback() {
        lateinit var newList: List<D>

        override fun getOldListSize() = mDataset.size

        override fun getNewListSize() = newList.size

        override fun areContentsTheSame(oldItemPosition : Int, newItemPosition : Int) = true

        override fun areItemsTheSame(oldItemPosition : Int, newItemPosition : Int) = mDataset[oldItemPosition] == newList[newItemPosition]
    }
}