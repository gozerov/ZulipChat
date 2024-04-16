package ru.gozerov.tfs_spring.screens.channels.chat.models

import ru.gozerov.tfs_spring.screens.channels.chat.adapters.message.Reaction

sealed interface ChatIntent {

    class RegisterEventQueue(
        val topic: String
    ): ChatIntent

    class LoadMessages(
        val stream: String,
        val topic: String
    ): ChatIntent

    class AddReaction(
        val messageId: Int,
        val emojiName: String,
        val emojiType: String,
        val emojiCode: String
    ): ChatIntent

    class UpdateReaction(
        val messageId: Int,
        val reaction: Reaction
    ): ChatIntent

    class SendMessage(
        val channel: String,
        val topic: String,
        val content: String
    ): ChatIntent

}