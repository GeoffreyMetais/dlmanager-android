package org.gmetais.downloadmanager.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import org.gmetais.downloadmanager.R
import org.gmetais.downloadmanager.data.File
import org.gmetais.downloadmanager.databinding.BrowserItemBinding
import org.gmetais.downloadmanager.getNameFromPath

class BrowserAdapter : BaseAdapter<File, BrowserItemBinding>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<BrowserItemBinding> {
        return ViewHolder(BrowserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder<BrowserItemBinding>, position: Int) {
        val item = dataset[position]
        holder.binding.itemName.text = item.path.getNameFromPath()
        holder.binding.itemIcon.setImageResource(if (item.isDirectory) R.drawable.ic_folder else R.drawable.ic_file)
    }
}
