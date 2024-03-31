package ru.gozerov.tfs_spring.screens.contacts.list.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.gozerov.tfs_spring.screens.contacts.list.models.UserContact

class ContactsDiffCallback(
    private val oldList: List<UserContact>,
    private val newList: List<UserContact>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}