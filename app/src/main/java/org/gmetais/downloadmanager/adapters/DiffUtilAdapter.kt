package org.gmetais.downloadmanager.adapters

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView


abstract class DiffUtilAdapter<D, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    var mDataset: List<D> = listOf()

    fun update (list: List<D>) {
        val result = DiffUtil.calculateDiff(DiffCallback(list), false)
        mDataset = list
        result.dispatchUpdatesTo(this@DiffUtilAdapter)
    }

    inner class DiffCallback(val newList: List<D>) : DiffUtil.Callback() {

        override fun getOldListSize() = mDataset.size

        override fun getNewListSize() = newList.size

        override fun areContentsTheSame(oldItemPosition : Int, newItemPosition : Int) = true

        override fun areItemsTheSame(oldItemPosition : Int, newItemPosition : Int) = mDataset[oldItemPosition] == newList[newItemPosition]
    }
}