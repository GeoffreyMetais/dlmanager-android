package org.gmetais.downloadmanager.adapters

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import org.gmetais.downloadmanager.SharedFile
import org.gmetais.downloadmanager.databinding.ShareItemBinding



class SharesAdapter(val handler: ShareHandler) : RecyclerView.Adapter<SharesAdapter.ViewHolder>() {

    var shares : List<SharedFile> = listOf()
    interface ShareHandler {
        fun open(share: SharedFile)
        fun delete(share: SharedFile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(ShareItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.item = shares[position]
    }

    override fun getItemCount() = shares.size

    inner class ViewHolder(val binding : ShareItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.handler = this@SharesAdapter.handler
        }
    }

    fun update (list: List<SharedFile>) {
        val sortedList = list.toList().sortedBy { it.name.toLowerCase() }
        val result = DiffUtil.calculateDiff(SharedFileDiffCallback(shares, sortedList), false)
        shares = sortedList
        result.dispatchUpdatesTo(this@SharesAdapter)
    }


    inner class SharedFileDiffCallback(val oldList: List<SharedFile>, val newList: List<SharedFile>) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areContentsTheSame(oldItemPosition : Int, newItemPosition : Int) = true

        override fun areItemsTheSame(oldItemPosition : Int, newItemPosition : Int) = oldList[oldItemPosition] == newList[newItemPosition]
    }
}