package org.gmetais.downloadmanager.ui.adapters

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import org.gmetais.downloadmanager.BR

abstract class BaseAdapter<D, B : ViewDataBinding>(val handler: Any) : DiffUtilAdapter<D, BaseAdapter.ViewHolder<B>>() {

    abstract fun getLayout(): Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder<B>(DataBindingUtil.inflate(LayoutInflater.from(parent.context), getLayout(), parent, false), handler)

    override fun onBindViewHolder(holder: ViewHolder<B>, position: Int) {
        holder.binding.setVariable(BR.item, mDataset[position])
    }

    override fun getItemCount() = mDataset.size

    class ViewHolder<out B : ViewDataBinding>(val binding : B, val handler: Any) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setVariable(BR.handler, handler)
        }
    }
}