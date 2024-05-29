package ru.gozerov.tfs_spring.data.cache.entities

import ru.gozerov.tfs_spring.data.remote.api.models.Message
import ru.gozerov.tfs_spring.data.remote.api.models.Stream
import ru.gozerov.tfs_spring.data.remote.api.models.StreamTopic
import ru.gozerov.tfs_spring.data.remote.api.models.UserContact
import ru.gozerov.tfs_spring.data.remote.api.models.ZulipReaction

fun StreamEntity.toStream() = Stream(id, title)
fun Stream.toStreamEntity(isFavorite: Boolean = false) = StreamEntity(stream_id, name, isFavorite)

fun TopicEntity.toTopic() = StreamTopic(title, maxId)
fun StreamTopic.toTopicEntity(streamId: Int) = TopicEntity(0, name, max_id, streamId)

fun Message.toMessageEntity(stream: String, topic: String) = MessageEntity(
    id,
    stream,
    topic,
    timestamp,
    avatar_url,
    sender_id,
    sender_full_name,
    is_me_message,
    content
)

fun MessageEntity.toMessage(reactions: List<ZulipReaction>) = Message(
    id,
    timestamp,
    avatarUrl,
    senderId,
    senderName,
    isMeMessage,
    content,
    reactions
)

fun UserEntity.toUserContact() = UserContact(id, imageUrl, username, email, isOnline)
fun UserContact.toUserEntity() = UserEntity(id, imageUrl, username, email, isOnline)