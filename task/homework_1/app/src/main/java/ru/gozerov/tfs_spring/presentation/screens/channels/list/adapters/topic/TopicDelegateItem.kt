package ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.topic

import ru.gozerov.tfs_spring.core.DelegateItem

class TopicDelegateItem(
    val id: Int,
    private val value: TopicModel
) : DelegateItem {
    override fun content(): Any = value

    override fun id(): Int = id

    override fun compareToOther(other: DelegateItem): Boolean =
        content() == (other as TopicDelegateItem).value
}