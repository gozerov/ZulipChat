package ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm.models

sealed interface ChatCommand {

    data class LoadChat(
        val stream: String,
        val topic: String,
        val fromCache: Boolean
    ) : ChatCommand

    data class SendMessage(
        val channel: String,
        val topic: String,
        val content: String
    ) : ChatCommand

    data class RegisterEventQueue(
        val topic: String
    ) : ChatCommand

    data class AddReaction(
        val messageId: Int,
        val emojiName: String
    ) : ChatCommand

    data class RemoveReaction(
        val messageId: Int,
        val emojiName: String
    ) : ChatCommand

    class GetEventsFromQueue : ChatCommand

    data object Exit : ChatCommand

}