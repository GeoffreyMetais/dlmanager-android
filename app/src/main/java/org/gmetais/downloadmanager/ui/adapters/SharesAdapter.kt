package org.gmetais.downloadmanager.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import org.gmetais.downloadmanager.NetworkHelper
import org.gmetais.downloadmanager.data.SharedFile
import org.gmetais.downloadmanager.databinding.ShareItemBinding
import org.gmetais.tools.ImageClick
import org.gmetais.tools.safeOffer

class SharesAdapter : BaseAdapter<SharedFile, ShareItemBinding>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<ShareItemBinding> {
        return ViewHolder(ShareItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)).apply {
            binding.itemDeleteIcon.setOnClickListener { eventsChannel.safeOffer(ImageClick(layoutPosition)) }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder<ShareItemBinding>, position: Int) {
        holder.binding.itemName.text = dataset[position].name
        val connected = NetworkHelper.getInstance(holder.itemView.context).connected.value ?: false
        holder.binding.itemDeleteIcon.isClickable = connected
        holder.itemView.isClickable = connected
    }
}