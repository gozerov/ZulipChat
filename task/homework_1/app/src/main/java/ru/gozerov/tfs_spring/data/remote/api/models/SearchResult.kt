package ru.gozerov.tfs_spring.data.remote.api.models

import ru.gozerov.tfs_spring.core.DelegateItem

data class SearchResult(
    val channels: Map<String, List<DelegateItem>>
)