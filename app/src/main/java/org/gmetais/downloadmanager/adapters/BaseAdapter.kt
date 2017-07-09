package org.gmetais.downloadmanager.adapters

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView

abstract class BaseAdapter<D, T : RecyclerView.ViewHolder> : RecyclerView.Adapter<T>() {

    var mDataset: List<D> = listOf()

    override fun getItemCount() = mDataset.size

    fun update (list: List<D>) {
        val result = DiffUtil.calculateDiff(DiffCallback(mDataset, list), false)
        mDataset = list
        result.dispatchUpdatesTo(this@BaseAdapter)
    }


    inner class DiffCallback(val oldList: List<D>, val newList: List<D>) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areContentsTheSame(oldItemPosition : Int, newItemPosition : Int) = true

        override fun areItemsTheSame(oldItemPosition : Int, newItemPosition : Int) = oldList[oldItemPosition] == newList[newItemPosition]
    }
}