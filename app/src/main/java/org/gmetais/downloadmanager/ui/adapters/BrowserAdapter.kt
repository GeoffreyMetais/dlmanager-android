package org.gmetais.downloadmanager.ui.adapters

import org.gmetais.downloadmanager.R
import org.gmetais.downloadmanager.data.File
import org.gmetais.downloadmanager.databinding.BrowserItemBinding

class BrowserAdapter(handler: IHandler) : BaseAdapter<File, BrowserItemBinding>(handler) {

    interface IHandler {
        fun open(file : File)
    }

    override fun getLayout() = R.layout.browser_item
}
