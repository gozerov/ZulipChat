package ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.date

import ru.gozerov.tfs_spring.core.DelegateItem

class DateDelegateItem(
    val id: Int,
    private val value: DateModel
) : DelegateItem {
    override fun content(): Any = value

    override fun id(): Int = id

    override fun compareToOther(other: DelegateItem): Boolean =
        content() == (other as DateDelegateItem).value
}