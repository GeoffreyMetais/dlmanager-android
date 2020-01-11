package org.gmetais.downloadmanager.ui.adapters

import androidx.viewbinding.ViewBinding
import org.gmetais.tools.*

abstract class BaseAdapter<D, B : ViewBinding>() : DiffUtilAdapter<D, BaseAdapter<D,B>.ViewHolder<B>>(),
        IEventsSource<Click> by EventsSource() {

    override fun getItemCount() = dataset.size

    inner class ViewHolder<B : ViewBinding>(val binding: B) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener { eventsChannel.safeOffer(SimpleClick(layoutPosition)) }
            itemView.setOnLongClickListener { eventsChannel.safeOffer(LongClick(layoutPosition)) }
        }
    }
}