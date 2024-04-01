package ru.gozerov.tfs_spring.screens.channels.list.adapters.channel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.gozerov.core.AdapterDelegate
import ru.gozerov.core.DelegateItem
import ru.gozerov.tfs_spring.R
import ru.gozerov.tfs_spring.databinding.ItemChannelBinding

class ChannelDelegate(
    private val onChannelClick: (channel: ChannelModel) -> Unit
) : AdapterDelegate, View.OnClickListener {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(
            ItemChannelBinding.inflate(
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
        (holder as ViewHolder).bind((item.content() as ChannelModel))
    }

    override fun isOfViewType(item: DelegateItem): Boolean = item is ChannelDelegateItem

    class ViewHolder(private val binding: ItemChannelBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ChannelModel) {
            binding.root.tag = data
            binding.txtTitle.text = data.title
            if (data.isExpanded)
                binding.imgArrow.setImageResource(R.drawable.ic_arrow_up_24)
            else
                binding.imgArrow.setImageResource(R.drawable.ic_arrow_down_24)
        }

    }

    override fun onClick(v: View?) {
        (v?.tag as? ChannelModel)?.let(onChannelClick)
    }

}