package ru.gozerov.tfs_spring.screens.channels.list.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.gozerov.core.DelegateItem
import ru.gozerov.tfs_spring.databinding.ItemChannelListBinding

class ChannelPagerAdapter(
    private val channelsAdapter: ChannelsAdapter
) : RecyclerView.Adapter<ChannelPagerAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemChannelListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: List<DelegateItem>) {
            channelsAdapter.submitList(data)
            binding.channelList.adapter = channelsAdapter
            binding.root.tag = data
        }

    }

    var data: List<List<DelegateItem>> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemChannelListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

}