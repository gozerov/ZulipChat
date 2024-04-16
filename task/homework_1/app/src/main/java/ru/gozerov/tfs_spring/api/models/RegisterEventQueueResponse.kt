package ru.gozerov.tfs_spring.api.models

data class RegisterEventQueueResponse(
    val queue_id: String,
    val last_event_id: Int
)
