package ru.gozerov.tfs_spring.api.models

data class GetSubscribedStreamsResponse(
    val subscriptions: List<Stream>
)