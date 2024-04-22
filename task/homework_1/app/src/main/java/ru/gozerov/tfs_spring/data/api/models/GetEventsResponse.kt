package ru.gozerov.tfs_spring.data.api.models

data class GetEventsResponse(
    val events: List<ZulipEvent>
)