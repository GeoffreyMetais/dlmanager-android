package org.gmetais.downloadmanager.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import org.gmetais.downloadmanager.R
import org.gmetais.downloadmanager.goTo
import org.gmetais.downloadmanager.ui.fragments.Browser

class PathAdapter(val browser: Browser, path: String) : androidx.recyclerview.widget.RecyclerView.Adapter<PathAdapter.ViewHolder>() {

    private val segments = path.replace("/home/geoffrey/Vidéos", "root").split('/').filter { !it.isEmpty() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.path_item, parent, false) as TextView)
    }

    override fun getItemCount() = segments.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.root.text = segments[position]
    }

    inner class ViewHolder(val root : TextView) : androidx.recyclerview.widget.RecyclerView.ViewHolder(root) {
        init {
            root.setOnClickListener { browser.goTo(segments[adapterPosition]) }
        }
    }
}