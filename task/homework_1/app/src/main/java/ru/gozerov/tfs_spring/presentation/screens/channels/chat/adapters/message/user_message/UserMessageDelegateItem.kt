package ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.message.user_message

import ru.gozerov.tfs_spring.core.DelegateItem

class UserMessageDelegateItem(
    val id: Int,
    private val value: UserMessageModel
) : DelegateItem {

    override fun content(): Any = value

    override fun id(): Int = id

    override fun compareToOther(other: DelegateItem): Boolean =
        (other as UserMessageDelegateItem).content() == content()

}