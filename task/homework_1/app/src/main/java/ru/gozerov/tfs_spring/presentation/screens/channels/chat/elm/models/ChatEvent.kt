package ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm.models

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.gozerov.tfs_spring.core.DelegateItem
import ru.gozerov.tfs_spring.data.remote.api.models.ZulipEvent
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.message.Reaction

sealed interface ChatEvent {

    sealed interface UI : ChatEvent {

        data class Init(
            val stream: String,
            val topic: String,
            val fromCache: Boolean = true
        ) : UI

        class RegisterEventQueue(
            val topic: String
        ) : UI

        class LoadMessages(
            val stream: String,
            val topic: String,
            val fromCache: Boolean = false
        ) : UI

        class SaveMessages(
            val data: PagingData<DelegateItem>
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
            val items: Flow<PagingData<DelegateItem>>,
            val fromCache: Boolean,
            val isFirstPage: Boolean
        ) : Internal

        object LoadChatError : Internal

        data class RegisteredEventQueue(
            val queueId: String,
            val lastEventId: Int
        ) : Internal

        data class NewEventsFromQueue(
            val items: List<ZulipEvent>,
            val userId: Int
        ) : Internal

    }

}