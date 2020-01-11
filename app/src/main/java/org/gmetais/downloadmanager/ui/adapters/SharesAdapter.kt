package org.gmetais.downloadmanager.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import org.gmetais.downloadmanager.NetworkHelper
import org.gmetais.downloadmanager.data.SharedFile
import org.gmetais.downloadmanager.databinding.ShareItemBinding
import org.gmetais.downloadmanager.lifecycleOwner
import org.gmetais.tools.ImageClick
import org.gmetais.tools.safeOffer

class SharesAdapter : BaseAdapter<SharedFile, ShareItemBinding>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ShareItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: BaseAdapter<SharedFile, ShareItemBinding>.ViewHolder<ShareItemBinding>, position: Int) {
        holder.binding.itemName.text = dataset[position].name
    }

    inner class ViewHolder(binding: ShareItemBinding) : BaseAdapter<SharedFile, ShareItemBinding>.ViewHolder<ShareItemBinding>(binding) {
        init {
            binding.itemDeleteIcon.setOnClickListener { eventsChannel.safeOffer(ImageClick(layoutPosition)) }
            NetworkHelper.getInstance(itemView.context).connected.observe(binding.lifecycleOwner, Observer {
                binding.itemDeleteIcon.isEnabled = it
                itemView.isEnabled = it
                binding.itemName.isEnabled = it
            })
        }
    }
}