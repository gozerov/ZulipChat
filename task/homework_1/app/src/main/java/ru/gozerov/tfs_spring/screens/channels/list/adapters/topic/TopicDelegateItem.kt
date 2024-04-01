package ru.gozerov.tfs_spring.screens.channels.list.adapters.topic

import ru.gozerov.core.DelegateItem

class TopicDelegateItem(
    val id: Int,
    private val value: TopicModel
) : DelegateItem {
    override fun content(): Any = value

    override fun id(): Int = id

    override fun compareToOther(other: DelegateItem): Boolean =
        content() == (other as TopicDelegateItem).value
}