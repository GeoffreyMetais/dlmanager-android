package org.gmetais.downloadmanager.ui.adapters

import org.gmetais.downloadmanager.R
import org.gmetais.downloadmanager.data.SharedFile

class SharesAdapter(handler: ShareHandler) : BaseAdapter<SharedFile>(handler) {

    interface ShareHandler {
        fun open(share: SharedFile)
        fun delete(share: SharedFile)
    }

    override fun getLayout() = R.layout.share_item
}