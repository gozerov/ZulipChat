package ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.gozerov.tfs_spring.core.AdapterDelegate
import ru.gozerov.tfs_spring.core.DelegateItem
import ru.gozerov.tfs_spring.core.DelegateItemCallback

class MainChatAdapter : ListAdapter<DelegateItem, ViewHolder>(DelegateItemCallback()) {

    private val delegates: MutableList<AdapterDelegate> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        delegates[viewType].onCreateViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        delegates[getItemViewType(position)].onBindViewHolder(holder, getItem(position), position)

    override fun getItemViewType(position: Int): Int {
        return delegates.indexOfFirst { it.isOfViewType(currentList[position]) }
    }

    fun addDelegate(delegate: AdapterDelegate) {
        delegates.add(delegate)
    }

}