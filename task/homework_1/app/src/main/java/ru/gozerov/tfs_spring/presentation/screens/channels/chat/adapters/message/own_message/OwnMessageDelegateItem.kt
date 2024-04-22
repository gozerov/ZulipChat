package ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.message.own_message

import ru.gozerov.tfs_spring.core.DelegateItem

class OwnMessageDelegateItem(
    val id: Int,
    private val value: OwnMessageModel
) : DelegateItem {

    override fun content(): Any = value

    override fun id(): Int = id

    override fun compareToOther(other: DelegateItem): Boolean =
        (other as OwnMessageDelegateItem).content() == content()

}