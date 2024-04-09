package ru.gozerov.tfs_spring.use_cases

import ru.gozerov.core.DelegateItem

data class SearchResult(
    val channels: Map<String, List<DelegateItem>>
)