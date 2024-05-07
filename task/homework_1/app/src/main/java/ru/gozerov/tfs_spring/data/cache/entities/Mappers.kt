package ru.gozerov.tfs_spring.data.cache.entities

import ru.gozerov.tfs_spring.data.remote.api.models.Stream
import ru.gozerov.tfs_spring.data.remote.api.models.StreamTopic


fun StreamEntity.toStream() = Stream(id, title)
fun Stream.toStreamEntity(isFavorite: Boolean = false) = StreamEntity(stream_id, name, isFavorite)

fun TopicEntity.toTopic() = StreamTopic(title, maxId)
fun StreamTopic.toTopicEntity(streamId: Int) = TopicEntity(0, name, max_id, streamId)