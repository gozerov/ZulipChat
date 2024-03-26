package ru.gozerov.core

import androidx.recyclerview.widget.DiffUtil

class DelegateItemCallback : DiffUtil.ItemCallback<DelegateItem>() {

    override fun areItemsTheSame(oldItem: DelegateItem, newItem: DelegateItem): Boolean =
        oldItem::class == newItem::class && oldItem.id() == newItem.id()


    override fun areContentsTheSame(oldItem: DelegateItem, newItem: DelegateItem): Boolean =
        oldItem.compareToOther(newItem)

    override fun getChangePayload(oldItem: DelegateItem, newItem: DelegateItem): Any? {
        if (oldItem.content() != newItem.content()) return newItem.content()
        return super.getChangePayload(oldItem, newItem)
    }

}