package ru.gozerov.tfs_spring.screens.channels.chat.adapters.user_message

import ru.gozerov.core.DelegateItem

class UserMessageDelegateItem(
    val id: Int,
    private val value: UserMessageModel
) : DelegateItem {

    override fun content(): Any = value

    override fun id(): Int = id

    override fun compareToOther(other: DelegateItem): Boolean =
        (other as UserMessageDelegateItem).content() == content()

}