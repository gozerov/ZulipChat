package ru.gozerov.tfs_spring.screens.channels.chat.adapters.message

import ru.gozerov.core.DelegateItem

class MessageDelegateItem(
    val id: Int,
    private val value: MessageModel
) : DelegateItem {

    override fun content(): Any = value

    override fun id(): Int = id

    override fun compareToOther(other: DelegateItem): Boolean =
        (other as MessageDelegateItem).content() == content()

}