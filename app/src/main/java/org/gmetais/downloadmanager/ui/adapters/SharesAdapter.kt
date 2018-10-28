package org.gmetais.downloadmanager.ui.adapters

import androidx.lifecycle.LiveData
import org.gmetais.downloadmanager.NetworkHelper
import org.gmetais.downloadmanager.R
import org.gmetais.downloadmanager.data.SharedFile
import org.gmetais.downloadmanager.databinding.ShareItemBinding

class SharesAdapter(handler: ShareHandler) : BaseAdapter<SharedFile, ShareItemBinding>(handler) {

    private lateinit var connected : LiveData<Boolean>

    interface ShareHandler {
        fun open(share: SharedFile) : Unit?
        fun delete(share: SharedFile)
    }

    override fun getLayout() = R.layout.share_item

    override fun onBindViewHolder(holder: ViewHolder<ShareItemBinding>, position: Int) {
        super.onBindViewHolder(holder, position)
        if (!this::connected.isInitialized) connected = NetworkHelper.getInstance(holder.itemView.context).connected
        holder.binding.connected = connected
    }
}