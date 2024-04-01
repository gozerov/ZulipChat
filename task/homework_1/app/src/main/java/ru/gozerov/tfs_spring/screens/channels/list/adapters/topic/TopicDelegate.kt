package ru.gozerov.tfs_spring.screens.channels.list.adapters.topic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.gozerov.core.AdapterDelegate
import ru.gozerov.core.DelegateItem
import ru.gozerov.tfs_spring.databinding.ItemTopicBinding

class TopicDelegate(
    private val onTopicClick: (topic: TopicModel) -> Unit
) : AdapterDelegate, View.OnClickListener {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(
            ItemTopicBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int
    ) {
        holder.itemView.setOnClickListener(this)
        (holder as ViewHolder).bind((item.content() as TopicModel))
    }

    override fun isOfViewType(item: DelegateItem): Boolean = item is TopicDelegateItem

    class ViewHolder(private val binding: ItemTopicBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: TopicModel) {
            with(binding) {
                root.tag = data
                txtTitle.text = data.title
                txtMessageCount.text = data.messageCount.toString()
                root.setBackgroundColor(data.color.toArgb())
            }
        }

    }

    override fun onClick(v: View?) {
        (v?.tag as? TopicModel)?.let { onTopicClick(it) }
    }

}