package org.gmetais.downloadmanager

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import org.gmetais.downloadmanager.databinding.ShareItemBinding

class SharesAdapter(val handler: ShareHandler, val shares : MutableList<SharedFile>) : RecyclerView.Adapter<SharesAdapter.ViewHolder>() {

    interface ShareHandler {
        fun open(share: SharedFile)
        fun delete(share: SharedFile)
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

    fun remove(name: String) : Boolean {
        val index = getItemPosition(name)
        if (index != -1) {
            shares.removeAt(index)
            notifyItemRemoved(index)
            return true
        }
        return false
    }

    private fun getItemPosition(name : String): Int {
        return shares.withIndex()
                .firstOrNull { it.value.name == name }
                ?.index
                ?: -1
    }

    inner class ViewHolder(val binding : ShareItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.handler = this@SharesAdapter.handler
        }
    }
}