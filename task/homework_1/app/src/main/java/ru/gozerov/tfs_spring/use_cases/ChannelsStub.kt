package ru.gozerov.tfs_spring.use_cases

import ru.gozerov.core.DelegateItem

object ChannelsStub {

    val categories = listOf("Subscribed", "All streams")

    var originalAllCombinedChannels: Map<String, List<DelegateItem>> = emptyMap()
    var allCombinedChannels = originalAllCombinedChannels

    var lastDate = ""


}