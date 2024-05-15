package ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm.models

sealed interface ChatCommand {

    class LoadChat(
        val stream: String,
        val topic: String,
        val fromCache: Boolean
    ) : ChatCommand

    class SendMessage(
        val channel: String,
        val topic: String,
        val content: String
    ) : ChatCommand

    class RegisterEventQueue(
        val topic: String
    ) : ChatCommand

    class AddReaction(
        val messageId: Int,
        val emojiName: String
    ) : ChatCommand

    class RemoveReaction(
        val messageId: Int,
        val emojiName: String
    ) : ChatCommand

    class GetEventsFromQueue(
        val queueId: String,
        val lastId: Int
    ) : ChatCommand

    object Exit : ChatCommand

}