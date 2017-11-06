package org.gmetais.downloadmanager.ui.adapters

import org.gmetais.downloadmanager.R
import org.gmetais.downloadmanager.data.SharedFile
import org.gmetais.downloadmanager.databinding.ShareItemBinding

class SharesAdapter(handler: ShareHandler) : BaseAdapter<SharedFile, ShareItemBinding>(handler) {

    interface ShareHandler {
        fun open(share: SharedFile) : Unit?
        fun delete(share: SharedFile)
    }

    override fun getLayout() = R.layout.share_item
}