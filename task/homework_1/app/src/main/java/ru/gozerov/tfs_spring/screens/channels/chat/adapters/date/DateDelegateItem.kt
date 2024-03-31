package ru.gozerov.tfs_spring.screens.channels.chat.adapters.date

import ru.gozerov.core.DelegateItem

class DateDelegateItem(
    val id: Int,
    private val value: DateModel
) : DelegateItem {
    override fun content(): Any = value

    override fun id(): Int = id

    override fun compareToOther(other: DelegateItem): Boolean =
        content() == (other as DateDelegateItem).value
}