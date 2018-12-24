package org.gmetais.downloadmanager.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import org.gmetais.downloadmanager.BR

abstract class BaseAdapter<D, B : ViewDataBinding>(val handler: Any) : DiffUtilAdapter<D, BaseAdapter.ViewHolder<B>>() {

    abstract fun getLayout(): Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder<B>(DataBindingUtil.inflate(LayoutInflater.from(parent.context), getLayout(), parent, false), handler)

    override fun onBindViewHolder(holder: ViewHolder<B>, position: Int) {
        holder.binding.setVariable(BR.item, dataset[position])
    }

    override fun getItemCount() = dataset.size

    class ViewHolder<out B : ViewDataBinding>(val binding : B, val handler: Any) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setVariable(BR.handler, handler)
        }
    }
}