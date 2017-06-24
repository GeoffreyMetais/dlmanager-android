package org.gmetais.downloadmanager

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import org.gmetais.downloadmanager.databinding.ShareItemBinding

class SharesAdapter(val handler: ShareHandler, val shares : List<SharedFile>) : RecyclerView.Adapter<SharesAdapter.ViewHolder>() {

    interface ShareHandler {
        fun open(share: SharedFile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SharesAdapter.ViewHolder {
        return ViewHolder(ShareItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.item = shares[position]
    }

    override fun getItemCount(): Int {
        return shares.size
    }

    inner class ViewHolder(val binding : ShareItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.handler = this@SharesAdapter.handler
        }
    }
}