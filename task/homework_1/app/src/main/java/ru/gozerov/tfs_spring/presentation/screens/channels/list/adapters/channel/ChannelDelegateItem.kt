package ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.channel

import ru.gozerov.tfs_spring.core.DelegateItem

class ChannelDelegateItem(
    val id: Int,
    private val value: ChannelModel
) : DelegateItem {
    override fun content(): Any = value

    override fun id(): Int = id

    override fun compareToOther(other: DelegateItem): Boolean =
        content() == (other as ChannelDelegateItem).value
}