package org.gmetais.downloadmanager.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.withContext

abstract class DiffUtilAdapter<D, VH : androidx.recyclerview.widget.RecyclerView.ViewHolder> : androidx.recyclerview.widget.RecyclerView.Adapter<VH>(), CoroutineScope {
    override val coroutineContext = Dispatchers.Main

    protected var dataset: List<D> = listOf()
    private val diffCallback by lazy(LazyThreadSafetyMode.NONE) { DiffCallback() }
    private val eventActor = actor<List<D>>(capacity = Channel.CONFLATED) { channel.consumeEach { internalUpdate(it) } }

    fun update (list: List<D>) = eventActor.offer(list)

    private suspend fun internalUpdate(list: List<D>) {
        val dataSet = list.toList()
        val result = withContext(Dispatchers.Default) { DiffUtil.calculateDiff(diffCallback.apply { newList = dataSet }, false) }
        dataset = dataSet
        result.dispatchUpdatesTo(this@DiffUtilAdapter)
    }

    private inner class DiffCallback : DiffUtil.Callback() {
        lateinit var newList: List<D>

        override fun getOldListSize() = dataset.size

        override fun getNewListSize() = newList.size

        override fun areContentsTheSame(oldItemPosition : Int, newItemPosition : Int) = true

        override fun areItemsTheSame(oldItemPosition : Int, newItemPosition : Int) = dataset[oldItemPosition] == newList[newItemPosition]
    }
}