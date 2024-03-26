package ru.gozerov.tfs_spring.screens.chat.dialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.gozerov.tfs_spring.databinding.ItemEmojiBinding

class SelectEmojiAdapter(
    private val onEmojiClick: (String) -> Unit
) : RecyclerView.Adapter<SelectEmojiAdapter.ViewHolder>(), View.OnClickListener {

    class ViewHolder(private val binding: ItemEmojiBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: String) {
            binding.root.text = data
        }

    }

    var data: List<String> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemEmojiBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener(this)
        holder.bind(data[position])
    }

    override fun onClick(v: View?) {
        (v as? TextView)?.let { textView ->
            this.onEmojiClick(textView.text.toString())
        }
    }

}