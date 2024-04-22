package ru.gozerov.tfs_spring.data.api.models

data class GetSubscribedStreamsResponse(
    val subscriptions: List<Stream>
)