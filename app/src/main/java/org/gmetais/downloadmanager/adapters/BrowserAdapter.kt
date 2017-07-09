package org.gmetais.downloadmanager.adapters

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import org.gmetais.downloadmanager.File
import org.gmetais.downloadmanager.databinding.BrowserItemBinding

class BrowserAdapter(val browserHandler: IHandler) : Adapter<BrowserAdapter.ViewHolder>() {

    var filesList : List<File> = listOf()

    interface IHandler {
        fun open(file : File)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(BrowserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.file = filesList[position]
    }

    override fun getItemCount() = filesList.size

    inner class ViewHolder(val binding : BrowserItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.browser = this@BrowserAdapter.browserHandler
        }
    }

    fun update (list: List<File>) {
        val result = DiffUtil.calculateDiff(SharedFileDiffCallback(filesList, list), false)
        filesList = list.toList()
        result.dispatchUpdatesTo(this@BrowserAdapter)
    }


    inner class SharedFileDiffCallback(val oldList: List<File>, val newList: List<File>) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areContentsTheSame(oldItemPosition : Int, newItemPosition : Int) = true

        override fun areItemsTheSame(oldItemPosition : Int, newItemPosition : Int) = oldList[oldItemPosition] == newList[newItemPosition]
    }
}
