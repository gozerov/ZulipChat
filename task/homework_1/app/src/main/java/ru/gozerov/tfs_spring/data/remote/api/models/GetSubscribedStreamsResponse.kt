package ru.gozerov.tfs_spring.data.remote.api.models

data class GetSubscribedStreamsResponse(
    val subscriptions: List<Stream>
)