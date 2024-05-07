package ru.gozerov.tfs_spring.data.remote.api.models

data class GetEventsResponse(
    val events: List<ZulipEvent>
)