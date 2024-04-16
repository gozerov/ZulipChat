package ru.gozerov.tfs_spring.screens.channels.chat.adapters.user_message

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.gozerov.core.AdapterDelegate
import ru.gozerov.core.DelegateItem
import ru.gozerov.core.views.UserMessageCardLayout
import ru.gozerov.tfs_spring.databinding.ItemUserMessageBinding
import ru.gozerov.tfs_spring.screens.channels.chat.adapters.message.MessageModel
import ru.gozerov.tfs_spring.screens.channels.chat.adapters.message.Reaction

class UserMessageDelegate(
    private val onLongClick: (messageId: Int) -> Unit,
    private val onReactionChanged: (messageId: Int, reaction: Reaction) -> Unit
) : AdapterDelegate, View.OnLongClickListener {

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
        .apply {
            holder.itemView.setOnLongClickListener(this@UserMessageDelegate)
            val messageCardLayout = holder.itemView as UserMessageCardLayout
            messageCardLayout.addOnAddButtonClickListener {
                onLongClick((item.content() as UserMessageModel).id)
            }
            messageCardLayout.addOnEmojiChangedListener {
                val message = item.content() as UserMessageModel
                onReactionChanged(
                    message.id,
                    Reaction(it.emojiName, it.emojiCode, it.emojiType, it.count, it.isEmojiSelected)
                )
            }
        }

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

    override fun onLongClick(v: View?): Boolean {
        (v?.tag as? UserMessageModel)?.let {
            onLongClick(it.id)
        }
        return true
    }

}