package ru.gozerov.tfs_spring.screens.channels.chat.adapters.user_message

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.gozerov.core.AdapterDelegate
import ru.gozerov.core.DelegateItem
import ru.gozerov.tfs_spring.databinding.ItemUserMessageBinding

class UserMessageDelegate : AdapterDelegate {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(
            ItemUserMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int
    ) = (holder as ViewHolder).bind((item.content() as UserMessageModel))

    override fun isOfViewType(item: DelegateItem): Boolean = item is UserMessageDelegateItem

    class ViewHolder(private val binding: ItemUserMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: UserMessageModel) {
            with(binding.root) {
                tag = data
                message = data.message
                addReaction(data.reactions)
            }
        }

    }

}