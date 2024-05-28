package ru.gozerov.tfs_spring.domain.models

import kotlinx.coroutines.flow.Flow
import ru.gozerov.tfs_spring.core.DelegateItem

data class ExpandTopicResult(
    val category: String,
    val items: List<DelegateItem>
)
