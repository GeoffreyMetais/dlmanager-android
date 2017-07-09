package org.gmetais.downloadmanager.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import org.gmetais.downloadmanager.SharedFile
import org.gmetais.downloadmanager.databinding.ShareItemBinding

class SharesAdapter(val handler: ShareHandler) : BaseAdapter<SharedFile, SharesAdapter.ViewHolder>() {

    interface ShareHandler {
        fun open(share: SharedFile)
        fun delete(share: SharedFile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(ShareItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.item = mDataset[position]
    }

    inner class ViewHolder(val binding : ShareItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.handler = this@SharesAdapter.handler
        }
    }
}