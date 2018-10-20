package org.gmetais.downloadmanager.ui.adapters

import androidx.annotation.WorkerThread
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.actor
import kotlinx.coroutines.experimental.withContext

abstract class DiffUtilAdapter<D, VH : androidx.recyclerview.widget.RecyclerView.ViewHolder> : androidx.recyclerview.widget.RecyclerView.Adapter<VH>() {

    protected var dataset: List<D> = listOf()
    private val diffCallback by lazy(LazyThreadSafetyMode.NONE) { DiffCallback() }
    private val eventActor = actor<List<D>>(capacity = Channel.CONFLATED) { for (list in channel) internalUpdate(list) }

    fun update (list: List<D>) = eventActor.offer(list)

    @WorkerThread
    private suspend fun internalUpdate(list: List<D>) {
        val dataSet = list.toList()
        val result = DiffUtil.calculateDiff(diffCallback.apply { newList = dataSet }, false)
        withContext(UI) {
            dataset = dataSet
            result.dispatchUpdatesTo(this@DiffUtilAdapter)
        }
    }

    private inner class DiffCallback : DiffUtil.Callback() {
        lateinit var newList: List<D>

        override fun getOldListSize() = dataset.size

        override fun getNewListSize() = newList.size

        override fun areContentsTheSame(oldItemPosition : Int, newItemPosition : Int) = true

        override fun areItemsTheSame(oldItemPosition : Int, newItemPosition : Int) = dataset[oldItemPosition] == newList[newItemPosition]
    }
}