package ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm

import android.util.Log
import androidx.paging.insertFooterItem
import androidx.paging.map
import kotlinx.coroutines.flow.flowOf
import ru.gozerov.tfs_spring.core.DelegateItem
import ru.gozerov.tfs_spring.core.utils.getEmojiByUnicode
import ru.gozerov.tfs_spring.core.utils.mapMonth
import ru.gozerov.tfs_spring.data.remote.api.models.Message
import ru.gozerov.tfs_spring.data.remote.api.models.MutableReaction
import ru.gozerov.tfs_spring.data.remote.api.models.ZulipEvent
import ru.gozerov.tfs_spring.domain.stubs.UserStub
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.date.DateDelegateItem
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.date.DateModel
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.message.Reaction
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.message.own_message.OwnMessageDelegateItem
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.message.own_message.OwnMessageModel
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.message.user_message.UserMessageDelegateItem
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.message.user_message.UserMessageModel
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm.models.ChatCommand
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm.models.ChatEffect
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm.models.ChatEvent
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm.models.ChatState
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer
import java.util.Calendar
import java.util.TimeZone

class ChatReducer : DslReducer<ChatEvent, ChatState, ChatEffect, ChatCommand>() {

    override fun Result.reduce(event: ChatEvent) = when (event) {
        is ChatEvent.Internal.LoadChatSuccess -> {
            state {
                copy(
                    isLoading = false,
                    fromCache = event.fromCache,
                    flowItems = event.items,
                    isFirstPage = event.isFirstPage
                )
            }
        }

        is ChatEvent.Internal.LoadChatError -> {
            state { copy(isLoading = false) }
            effects { +ChatEffect.ShowError }
        }

        is ChatEvent.UI.Init -> {
            state { copy(isLoading = true) }
            commands {
                +ChatCommand.LoadChat(event.stream, event.topic, event.fromCache)
                +ChatCommand.RegisterEventQueue(event.topic)
            }
        }

        is ChatEvent.UI.LoadMessages -> {
            state { copy(isLoading = true) }
            commands { +ChatCommand.LoadChat(event.stream, event.topic, event.fromCache) }
        }

        is ChatEvent.UI.SaveMessages -> {
            state { copy(flowItems = null, items = event.data, isFirstPage = false) }
        }

        is ChatEvent.UI.SendMessage -> {
            commands { +ChatCommand.SendMessage(event.channel, event.topic, event.content) }
        }

        is ChatEvent.UI.RegisterEventQueue -> {
            commands { +ChatCommand.RegisterEventQueue(event.topic) }
        }

        is ChatEvent.UI.AddReaction -> {
            commands {
                +ChatCommand.AddReaction(event.messageId, event.emojiName)
            }
            addReaction(event)
        }

        is ChatEvent.UI.UpdateReaction -> {
            commands {
                if (event.reaction.isSelected)
                    +ChatCommand.RemoveReaction(event.messageId, event.reaction.emojiName)
                else
                    +ChatCommand.AddReaction(event.messageId, event.reaction.emojiName)
            }
            updateReaction(event)
        }

        is ChatEvent.Internal.RegisteredEventQueue -> {
            commands { +ChatCommand.GetEventsFromQueue() }
        }

        is ChatEvent.Internal.NewEventsFromQueue -> {
            obtainNewMessages(event.items)
        }

        is ChatEvent.UI.Exit -> {
            commands { +ChatCommand.Exit }
        }
    }

    // inner logic

    private fun Result.addReaction(event: ChatEvent.UI.AddReaction) {
        val newItems = state.items?.map { item ->
            if (item is UserMessageDelegateItem && item.id == event.messageId) {
                val message = item.content() as UserMessageModel
                var exists = false
                val reactions = message.reactions.map { reaction ->
                    if (reaction.emojiName == event.emojiName) {
                        exists = true
                        if (reaction.isSelected)
                            reaction
                        else {
                            reaction.copy(count = reaction.count + 1, isSelected = true)
                        }
                    } else reaction
                }.toMutableList()
                if (!exists) {
                    reactions.add(
                        Reaction(event.emojiName, event.emojiCode, 1, true)
                    )
                }
                UserMessageDelegateItem(
                    item.id,
                    message.copy(reactions = reactions.toList())
                )
            } else if (item is OwnMessageDelegateItem && item.id == event.messageId) {
                val message = item.content() as OwnMessageModel
                var exists = false
                val reactions = message.reactions.map { reaction ->
                    if (reaction.emojiName == event.emojiName) {
                        exists = true
                        if (reaction.isSelected)
                            reaction
                        else {
                            reaction.copy(count = reaction.count + 1, isSelected = true)
                        }
                    } else reaction
                }.toMutableList()
                if (!exists) {

                    reactions.add(
                        Reaction(event.emojiName, event.emojiCode, 1, true)
                    )
                }
                OwnMessageDelegateItem(
                    item.id(),
                    message.copy(reactions = reactions.toList())
                )
            } else item
        }
        newItems?.let {
            state { copy(flowItems = flowOf(newItems), items = newItems) }
        }
    }

    private fun Result.updateReaction(event: ChatEvent.UI.UpdateReaction) {
        state.items?.let { messagesWithDate ->
            val newItems = messagesWithDate.map {
                if ((it is UserMessageDelegateItem || it is OwnMessageDelegateItem) && it.id() == event.messageId) {
                    if (it is UserMessageDelegateItem) {
                        val message = it.content() as UserMessageModel
                        val reactions = message.reactions.mapNotNull { reaction ->
                            if (reaction.emojiName == event.reaction.emojiName) {
                                if (reaction.count == 1 && reaction.isSelected) {
                                    null
                                } else if (reaction.isSelected) {
                                    reaction.copy(
                                        count = event.reaction.count - 1,
                                        isSelected = !event.reaction.isSelected
                                    )
                                } else {
                                    reaction.copy(
                                        count = event.reaction.count + 1,
                                        isSelected = !event.reaction.isSelected
                                    )
                                }
                            } else reaction
                        }
                        UserMessageDelegateItem(it.id, message.copy(reactions = reactions))
                    } else {
                        val message = it.content() as OwnMessageModel
                        val reactions = message.reactions.mapNotNull { reaction ->
                            if (reaction.emojiName == event.reaction.emojiName) {
                                if (reaction.count == 1 && reaction.isSelected) {
                                    null
                                } else if (reaction.isSelected) {
                                    reaction.copy(
                                        count = event.reaction.count - 1,
                                        isSelected = !event.reaction.isSelected
                                    )
                                } else {
                                    reaction.copy(
                                        count = event.reaction.count + 1,
                                        isSelected = !event.reaction.isSelected
                                    )
                                }
                            } else reaction
                        }
                        OwnMessageDelegateItem(it.id(), message.copy(reactions = reactions))
                    }

                } else it
            }
            state { copy(flowItems = flowOf(newItems), items = newItems) }
        }
    }


    private fun Result.obtainNewMessages(items: List<ZulipEvent>) {
        val messages = mutableListOf<Message>()

        items.forEach {
            if (it.type == "message") {
                messages.add(it.message)
            }
        }
        var lastDate = ""
        state.items?.map { item ->
            if (item is DateDelegateItem) {
                lastDate = (item.content() as DateModel).date
            }
            item
        }
        var dateCount = 0
        val calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getTimeZone("Europe/Moscow")
        val messageItems = mutableListOf<DelegateItem>()
        messages.map { message ->
            val reactionCodes =
                mutableMapOf<String, MutableReaction>()
            message.reactions.forEach { reaction ->
                val listR = reactionCodes[reaction.emoji_name]
                listR?.let {
                    it.count++
                    if (reaction.user_id == UserStub.CURRENT_USER_ID)
                        it.isSelected = true
                } ?: reactionCodes.put(
                    reaction.emoji_name,
                    MutableReaction(
                        reaction.emoji_name,
                        reaction.reaction_type,
                        reaction.emoji_code,
                        1,
                        reaction.user_id == UserStub.CURRENT_USER_ID
                    )
                )
            }
            calendar.timeInMillis = message.timestamp * 1000
            val date =
                calendar.get(Calendar.DAY_OF_MONTH).toString() + " " + mapMonth(
                    calendar.get(
                        Calendar.MONTH
                    )
                )
            if (date != lastDate) {
                lastDate = date
                dateCount++
                messageItems.add(DateDelegateItem(dateCount, DateModel(dateCount, lastDate)))
            }
            val immutableReactions = reactionCodes.values.map {
                Reaction(
                    it.emojiName,
                    getEmojiByUnicode(it.emojiCode, it.type),
                    it.count,
                    it.isSelected
                )
            }
            if (message.sender_id == UserStub.CURRENT_USER_ID) {
                messageItems.add(
                    OwnMessageDelegateItem(
                        message.id,
                        OwnMessageModel(message.id, message.content, immutableReactions)
                    )
                )
            } else {
                messageItems.add(
                    UserMessageDelegateItem(
                        message.id,
                        UserMessageModel(
                            message.id,
                            message.sender_full_name,
                            message.sender_id,
                            message.content,
                            immutableReactions,
                            message.avatar_url
                        )
                    )
                )
            }
        }
        state.items?.let { data ->
            var newData = data
            messageItems.forEach {
                newData = data.insertFooterItem(item = it)
            }
            state { copy(items = newData, flowItems = flowOf(newData)) }
        }
        commands {
            +ChatCommand.GetEventsFromQueue()
        }
    }

}