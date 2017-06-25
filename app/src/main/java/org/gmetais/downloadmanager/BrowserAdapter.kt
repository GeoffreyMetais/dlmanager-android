package org.gmetais.downloadmanager

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import org.gmetais.downloadmanager.databinding.BrowserItemBinding

class BrowserAdapter(val browserHandler: IHandler, val filesList : List<File>) : Adapter<BrowserAdapter.ViewHolder>() {

    interface IHandler {
        fun open(file : File)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrowserAdapter.ViewHolder {
        return ViewHolder(BrowserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: BrowserAdapter.ViewHolder, position: Int) {
        holder.binding.file = filesList[position]
    }

    override fun getItemCount(): Int {
        return filesList.size
    }

    inner class ViewHolder(val binding : BrowserItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.browser = this@BrowserAdapter.browserHandler
        }
    }
}
