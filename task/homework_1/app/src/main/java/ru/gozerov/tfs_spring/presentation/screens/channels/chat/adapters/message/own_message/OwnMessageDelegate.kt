package ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.message.own_message

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.gozerov.tfs_spring.core.AdapterDelegate
import ru.gozerov.tfs_spring.core.DelegateItem
import ru.gozerov.tfs_spring.core.views.UserMessageCardLayout
import ru.gozerov.tfs_spring.databinding.ItemUserMessageBinding
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.message.Reaction

class OwnMessageDelegate(
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
    ) = (holder as ViewHolder).bind((item.content() as OwnMessageModel))
        .apply {
            holder.itemView.setOnLongClickListener(this@OwnMessageDelegate)
            val messageCardLayout = holder.itemView as UserMessageCardLayout
            messageCardLayout.addOnAddButtonClickListener {
                onLongClick((item.content() as OwnMessageModel).id)
            }
            messageCardLayout.addOnEmojiChangedListener {
                val message = item.content() as OwnMessageModel
                onReactionChanged(
                    message.id,
                    Reaction(it.emojiName, it.emojiCode, it.count, it.isEmojiSelected)
                )
            }
        }

    override fun isOfViewType(item: DelegateItem): Boolean = item is OwnMessageDelegateItem

    class ViewHolder(private val binding: ItemUserMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: OwnMessageModel) {
            with(binding.root) {
                tag = data
                message = data.message
                addReaction(data.reactions)
            }
        }

    }

    override fun onLongClick(v: View?): Boolean {
        (v?.tag as? OwnMessageModel)?.let {
            onLongClick(it.id)
        }
        return true
    }

}