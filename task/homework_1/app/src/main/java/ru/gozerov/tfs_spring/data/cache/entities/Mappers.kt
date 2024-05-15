package ru.gozerov.tfs_spring.data.cache.entities

import ru.gozerov.tfs_spring.data.remote.api.models.Message
import ru.gozerov.tfs_spring.data.remote.api.models.Stream
import ru.gozerov.tfs_spring.data.remote.api.models.StreamTopic
import ru.gozerov.tfs_spring.data.remote.api.models.ZulipReaction

fun StreamEntity.toStream() = Stream(id, title)
fun Stream.toStreamEntity(isFavorite: Boolean = false) = StreamEntity(stream_id, name, isFavorite)

fun TopicEntity.toTopic() = StreamTopic(title, maxId)
fun StreamTopic.toTopicEntity(streamId: Int) = TopicEntity(0, name, max_id, streamId)

fun Message.toMessageEntity(stream: String, topic: String) = MessageEntity(id, stream, topic, timestamp, avatar_url, sender_id, sender_full_name, is_me_message, content)
fun MessageEntity.toMessage(reactions: List<ZulipReaction>) = Message(id, timestamp, avatar_url, sender_id, sender_full_name, is_me_message, content, reactions)

fun ZulipReaction.toReactionEntity() = ReactionEntity(emoji_name, emoji_code, 1, isSelected = false, user_id)
fun ReactionEntity.toZulipReaction() = ZulipReaction(emojiName, reaction_type = "", emojiCode, userId)