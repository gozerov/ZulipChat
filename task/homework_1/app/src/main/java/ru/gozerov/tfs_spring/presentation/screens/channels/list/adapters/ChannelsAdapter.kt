package ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.gozerov.tfs_spring.core.AdapterDelegate
import ru.gozerov.tfs_spring.core.DelegateItem
import ru.gozerov.tfs_spring.core.DelegateItemCallback

class ChannelsAdapter : ListAdapter<DelegateItem, RecyclerView.ViewHolder>(DelegateItemCallback()) {

    private val delegates: MutableList<AdapterDelegate> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        delegates[viewType].onCreateViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        delegates[getItemViewType(position)].onBindViewHolder(holder, getItem(position), position)

    override fun getItemViewType(position: Int): Int {
        return delegates.indexOfFirst { it.isOfViewType(currentList[position]) }
    }

    fun addDelegate(delegate: AdapterDelegate) {
        delegates.add(delegate)
    }

}