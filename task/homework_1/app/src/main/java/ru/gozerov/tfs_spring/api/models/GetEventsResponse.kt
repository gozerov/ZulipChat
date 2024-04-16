package ru.gozerov.tfs_spring.api.models

data class GetEventsResponse(
    val events: List<ZulipEvent>
)