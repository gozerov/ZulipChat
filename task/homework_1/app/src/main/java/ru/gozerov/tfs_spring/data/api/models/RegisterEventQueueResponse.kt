package ru.gozerov.tfs_spring.data.api.models

data class RegisterEventQueueResponse(
    val queue_id: String,
    val last_event_id: Int
)
