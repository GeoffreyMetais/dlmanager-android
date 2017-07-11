package org.gmetais.downloadmanager.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import org.gmetais.downloadmanager.data.File
import org.gmetais.downloadmanager.databinding.BrowserItemBinding

class BrowserAdapter(val browserHandler: IHandler) : BaseAdapter<File, BrowserAdapter.ViewHolder>() {

    interface IHandler {
        fun open(file : File)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(BrowserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.file = mDataset[position]
    }

    inner class ViewHolder(val binding : BrowserItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.browser = this@BrowserAdapter.browserHandler
        }
    }
}
