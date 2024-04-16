package ru.gozerov.tfs_spring.screens.channels.chat.adapters.message

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import ru.gozerov.core.AdapterDelegate
import ru.gozerov.core.DelegateItem
import ru.gozerov.core.views.MessageCardLayout
import ru.gozerov.tfs_spring.databinding.ItemMessageBinding

class MessageDelegate(
    private val onLongClick: (messageId: Int) -> Unit,
    private val onReactionChanged: (messageId: Int, reaction: Reaction) -> Unit
) : AdapterDelegate, View.OnLongClickListener {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(
            ItemMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int
    ) = (holder as ViewHolder).bind((item.content() as MessageModel))
        .apply {
            holder.itemView.setOnLongClickListener(this@MessageDelegate)
            val messageCardLayout = holder.itemView as MessageCardLayout
            messageCardLayout.addOnAddButtonClickListener {
                onLongClick((item.content() as MessageModel).id)
            }
            messageCardLayout.addOnEmojiChangedListener {
                val message = item.content() as MessageModel
                onReactionChanged(
                    message.id,
                    Reaction(it.emojiName, it.emojiCode, it.emojiType, it.count, it.isEmojiSelected)
                )
            }
        }

    override fun isOfViewType(item: DelegateItem): Boolean = item is MessageDelegateItem

    class ViewHolder(private val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: MessageModel) {
            with(binding.root) {
                tag = data
                username = data.author
                message = data.message
                /*imageView.load(data.avatarUrl) {
                    transformations(CircleCropTransformation())
                }*/
                addReaction(data.reactions)
            }
        }

    }

    override fun onLongClick(v: View?): Boolean {
        (v?.tag as? MessageModel)?.let {
            onLongClick(it.id)
        }
        return true
    }

}