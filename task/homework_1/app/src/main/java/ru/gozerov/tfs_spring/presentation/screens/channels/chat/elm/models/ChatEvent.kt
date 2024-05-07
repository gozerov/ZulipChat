package ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm.models

import androidx.paging.PagingData
import ru.gozerov.tfs_spring.core.DelegateItem
import ru.gozerov.tfs_spring.data.remote.api.models.ZulipEvent
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.message.Reaction

sealed interface ChatEvent {

    sealed interface UI : ChatEvent {

        class Init(
            val stream: String,
            val topic: String
        ) : UI

        class RegisterEventQueue(
            val topic: String
        ) : UI

        class LoadMessages(
            val stream: String,
            val topic: String
        ) : UI

        class AddReaction(
            val messageId: Int,
            val emojiName: String,
            val emojiCode: String
        ) : UI

        class UpdateReaction(
            val messageId: Int,
            val reaction: Reaction
        ) : UI

        class SendMessage(
            val channel: String,
            val topic: String,
            val content: String
        ) : UI

        object Exit : UI

    }

    sealed interface Internal : ChatEvent {

        data class LoadChatSuccess(
            val items: PagingData<DelegateItem>,
            val positionToScroll: Int? = null
        ) : Internal

        object LoadChatError : Internal

        data class RegisteredEventQueue(
            val queueId: String,
            val lastEventId: Int
        ) : Internal

        data class NewEventsFromQueue(
            val items: List<ZulipEvent>
        ) : Internal

    }

}