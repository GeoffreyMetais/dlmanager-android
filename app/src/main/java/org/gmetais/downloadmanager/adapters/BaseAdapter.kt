package org.gmetais.downloadmanager.adapters

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import org.gmetais.downloadmanager.BR

abstract class BaseAdapter<D>(val handler: Any) : DiffUtilAdapter<D, BaseAdapter.ViewHolder>() {

    abstract fun getLayout(): Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), getLayout(), parent, false), handler)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.setVariable(BR.item, mDataset[position])
    }

    override fun getItemCount() = mDataset.size

    class ViewHolder(val binding : ViewDataBinding, val handler: Any) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setVariable(BR.handler, handler)
        }
    }
}