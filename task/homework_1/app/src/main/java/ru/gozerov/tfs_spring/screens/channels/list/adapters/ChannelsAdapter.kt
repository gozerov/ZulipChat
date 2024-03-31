package ru.gozerov.tfs_spring.screens.channels.list.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.gozerov.tfs_spring.R
import ru.gozerov.tfs_spring.databinding.ItemChannelBinding
import ru.gozerov.tfs_spring.databinding.ItemTopicBinding
import ru.gozerov.tfs_spring.screens.channels.list.models.Channel
import ru.gozerov.tfs_spring.screens.channels.list.models.Topic

class ChannelsAdapter(
    private val onChannelClick: (channel: Channel) -> Unit,
    private val onTopicClick: (channelTitle: String, id: Int) -> Unit
) : RecyclerView.Adapter<ChannelsAdapter.ViewHolder>(), View.OnClickListener {

    abstract class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        abstract fun bind(data: ChannelData)

    }

    class ParentViewHolder(private val binding: ItemChannelBinding) : ViewHolder(binding.root) {
        override fun bind(data: ChannelData) {
            val channel = data as Channel
            binding.root.tag = channel
            binding.txtTitle.text = channel.title
            if (channel.isExpanded)
                binding.imgArrow.setImageResource(R.drawable.ic_arrow_up_24)
            else
                binding.imgArrow.setImageResource(R.drawable.ic_arrow_down_24)
        }
    }

    class ChildViewHolder(private val binding: ItemTopicBinding) : ViewHolder(binding.root) {
        override fun bind(data: ChannelData) {
            val topic = data as Topic
            with(binding) {
                root.tag = topic
                txtTitle.text = topic.title
                txtMessageCount.text = topic.messageCount.toString()
                root.setBackgroundColor(data.color.toArgb())
            }
        }
    }

    var data: List<ChannelData> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is Channel -> TYPE_CHANNEL
            is Topic -> TYPE_TOPIC
            else -> error("Unknown viewType")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_CHANNEL -> ParentViewHolder(
                ItemChannelBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            )

            TYPE_TOPIC -> ChildViewHolder(
                ItemTopicBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            )

            else -> error("Unknown viewType")
        }
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener(this)
    }

    companion object {

        private const val TYPE_CHANNEL = 0
        private const val TYPE_TOPIC = 1

    }

    override fun onClick(v: View?) {
        when (val channelData = v?.tag) {
            is Channel -> {
                onChannelClick(channelData)
            }
            is Topic -> {
                val channel = data.first { it is Channel && it.id == channelData.channelId } as Channel
                onTopicClick(channel.title, channelData.id)
            }
        }
    }

}