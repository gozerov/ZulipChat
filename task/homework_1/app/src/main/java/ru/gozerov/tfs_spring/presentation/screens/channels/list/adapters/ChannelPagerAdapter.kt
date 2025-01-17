package ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.gozerov.tfs_spring.core.DelegateItem
import ru.gozerov.tfs_spring.databinding.ItemChannelListBinding

class ChannelPagerAdapter(
    private val channelAdapters: List<ChannelsAdapter>
) : RecyclerView.Adapter<ChannelPagerAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemChannelListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int, data: List<DelegateItem>) {
            channelAdapters[position].submitList(data)
            binding.channelList.adapter = channelAdapters[position]
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
        holder.bind(position, data[position])
    }

}