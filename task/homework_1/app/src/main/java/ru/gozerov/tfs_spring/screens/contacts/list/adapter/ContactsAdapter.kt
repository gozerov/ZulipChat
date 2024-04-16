package ru.gozerov.tfs_spring.screens.contacts.list.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import ru.gozerov.tfs_spring.R
import ru.gozerov.tfs_spring.databinding.ItemContactBinding
import ru.gozerov.tfs_spring.screens.contacts.list.models.UserContact

class ContactsAdapter(
    private val onItemClick: (id: Int) -> Unit
) : RecyclerView.Adapter<ContactsAdapter.ViewHolder>(), View.OnClickListener {

    class ViewHolder(private val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: UserContact) {
            with(binding) {
                root.tag = data
                imgContact.load(data.imageUrl) {
                    transformations(CircleCropTransformation())
                }
                txtUsername.text = data.username
                txtEmail.text = data.email
                Log.e("AAAA", data.isOnline.toString())
                imgOnline.setBackgroundResource(if (data.isOnline) R.drawable.online_bg else R.drawable.offline_bg)
            }
        }

    }

    var data: List<UserContact> = emptyList()
        set(value) {
            val diffCallback = ContactsDiffCallback(field, value)
            val result = DiffUtil.calculateDiff(diffCallback)
            field = value
            result.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemContactBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener(this)
        holder.bind(data[position])
    }

    override fun onClick(v: View?) {
        (v?.tag as? UserContact)?.let {
            onItemClick(it.id)
        }
    }

}