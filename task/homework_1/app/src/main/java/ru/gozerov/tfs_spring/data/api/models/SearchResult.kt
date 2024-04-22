package ru.gozerov.tfs_spring.data.api.models

import ru.gozerov.tfs_spring.core.DelegateItem

data class SearchResult(
    val channels: Map<String, List<DelegateItem>>
)