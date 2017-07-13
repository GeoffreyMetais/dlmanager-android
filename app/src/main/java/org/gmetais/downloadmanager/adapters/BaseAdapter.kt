package org.gmetais.downloadmanager.adapters

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import org.gmetais.downloadmanager.BR

abstract class BaseAdapter<D>(val handler: Any) : RecyclerView.Adapter<BaseAdapter.ViewHolder>() {

    abstract fun getLayout(): Int

    var mDataset: List<D> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), getLayout(), parent, false), handler)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.setVariable(BR.item, mDataset[position])
    }

    override fun getItemCount() = mDataset.size

    fun update (list: List<D>) {
        val result = DiffUtil.calculateDiff(DiffCallback(mDataset, list), false)
        mDataset = list
        result.dispatchUpdatesTo(this@BaseAdapter)
    }

    class ViewHolder(val binding : ViewDataBinding, val handler: Any) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setVariable(BR.handler, handler)
        }
    }

    inner class DiffCallback(val oldList: List<D>, val newList: List<D>) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areContentsTheSame(oldItemPosition : Int, newItemPosition : Int) = true

        override fun areItemsTheSame(oldItemPosition : Int, newItemPosition : Int) = oldList[oldItemPosition] == newList[newItemPosition]
    }
}