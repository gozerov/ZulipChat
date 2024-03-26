package ru.gozerov.tfs_spring.screens.chat.adapters.date

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.gozerov.core.AdapterDelegate
import ru.gozerov.core.DelegateItem
import ru.gozerov.tfs_spring.databinding.ItemDateBinding

class DateDelegate : AdapterDelegate {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(
            ItemDateBinding.inflate(
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
        (holder as ViewHolder).bind((item.content() as DateModel))
    }

    override fun isOfViewType(item: DelegateItem): Boolean = item is DateDelegateItem

    class ViewHolder(private val binding: ItemDateBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: DateModel) {
            binding.txtDate.text = data.date
        }

    }

}