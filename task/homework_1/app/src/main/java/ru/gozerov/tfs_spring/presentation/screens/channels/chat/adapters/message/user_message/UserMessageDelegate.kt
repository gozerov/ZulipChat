package ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.message.user_message

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ru.gozerov.tfs_spring.R
import ru.gozerov.tfs_spring.core.AdapterDelegate
import ru.gozerov.tfs_spring.core.DelegateItem
import ru.gozerov.tfs_spring.core.views.MessageCardLayout
import ru.gozerov.tfs_spring.databinding.ItemMessageBinding
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.message.Reaction

class UserMessageDelegate(
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
    ) = (holder as ViewHolder).bind((item.content() as UserMessageModel))
        .apply {
            holder.itemView.setOnLongClickListener(this@UserMessageDelegate)
            val messageCardLayout = holder.itemView as MessageCardLayout
            messageCardLayout.addOnAddButtonClickListener {
                onLongClick((item.content() as UserMessageModel).id)
            }
            messageCardLayout.addOnEmojiChangedListener {
                val message = item.content() as UserMessageModel
                onReactionChanged(
                    message.id,
                    Reaction(it.emojiName, it.emojiCode, it.count, it.isEmojiSelected)
                )
            }
        }

    override fun isOfViewType(item: DelegateItem): Boolean = item is UserMessageDelegateItem

    class ViewHolder(private val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: UserMessageModel) {
            with(binding.root) {
                tag = data
                username = data.author
                message = data.message
                Glide.with(context)
                    .load(data.avatarUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .circleCrop()
                    .into(imageView)
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