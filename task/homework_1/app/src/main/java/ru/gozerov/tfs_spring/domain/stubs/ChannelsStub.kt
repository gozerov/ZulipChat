package ru.gozerov.tfs_spring.domain.stubs

import ru.gozerov.tfs_spring.core.DelegateItem

object ChannelsStub {

    val categories = listOf("Subscribed", "All streams")

    var originalAllCombinedChannels: Map<String, List<DelegateItem>> = emptyMap()
    var allCombinedChannels = originalAllCombinedChannels

    var lastDate = ""

    var fromCache: Boolean? = false


}