package ru.gozerov.tfs_spring.data.remote.api.models

data class FullStream(
    val stream: Stream,
    val topics: List<StreamTopic>
)
