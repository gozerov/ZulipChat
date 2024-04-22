package ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm

import ru.gozerov.tfs_spring.core.DelegateItem
import ru.gozerov.tfs_spring.core.utils.getEmojiByUnicode
import ru.gozerov.tfs_spring.core.utils.mapMonth
import ru.gozerov.tfs_spring.domain.stubs.ChannelsStub
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
        is ChatEvent.UI.Init -> {
            state { copy(isLoading = true) }
            commands {
                +ChatCommand.LoadChat(event.stream, event.topic)
                +ChatCommand.RegisterEventQueue(event.topic)
            }
        }

        is ChatEvent.Internal.LoadChatSuccess -> {
            state {
                copy(
                    isLoading = false,
                    items = event.items,
                    positionToScroll = event.items.size - 1
                )
            }
        }

        is ChatEvent.Internal.LoadChatError -> {
            state { copy(isLoading = false) }
            effects { +ChatEffect.ShowError }
        }

        is ChatEvent.UI.LoadMessages -> {
            state { copy(isLoading = true) }
            commands { +ChatCommand.LoadChat(event.stream, event.topic) }
        }

        is ChatEvent.UI.SendMessage -> {
            commands { +ChatCommand.SendMessage(event.channel, event.topic, event.content) }
        }

        is ChatEvent.UI.RegisterEventQueue -> {
            commands { +ChatCommand.RegisterEventQueue(event.topic) }
        }

        is ChatEvent.UI.AddReaction -> {
            addReaction(event)
        }

        is ChatEvent.UI.UpdateReaction -> {
            updateReaction(event)
        }

        is ChatEvent.Internal.RegisteredEventQueue -> {
            ru.gozerov.tfs_spring.data.api.EventQueueData.lastId = event.lastEventId
            ru.gozerov.tfs_spring.data.api.EventQueueData.queueId = event.queueId
            commands { +ChatCommand.GetEventsFromQueue(event.queueId, event.lastEventId) }
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
        state.items?.let { messagesWithDate ->
            val newItems = messagesWithDate.map { item ->
                if (((item is UserMessageDelegateItem || item is OwnMessageDelegateItem) && item.id() == event.messageId)) {
                    if (item is UserMessageDelegateItem) {
                        val message = item.content() as UserMessageModel
                        var exists = false
                        val reactions = message.reactions.map { reaction ->
                            if (reaction.emojiName == event.emojiName) {
                                exists = true
                                if (reaction.isSelected)
                                    reaction
                                else {
                                    commands {
                                        +ChatCommand.AddReaction(
                                            event.messageId,
                                            event.emojiName
                                        )
                                    }
                                    reaction.copy(count = reaction.count + 1, isSelected = true)
                                }
                            } else reaction
                        }.toMutableList()
                        if (!exists) {
                            commands {
                                +ChatCommand.AddReaction(event.messageId, event.emojiName)
                            }
                            reactions.add(
                                Reaction(event.emojiName, event.emojiCode, 1, true)
                            )
                        }
                        UserMessageDelegateItem(
                            item.id,
                            message.copy(reactions = reactions.toList())
                        )
                    } else {
                        val message = item.content() as OwnMessageModel
                        var exists = false
                        val reactions = message.reactions.map { reaction ->
                            if (reaction.emojiName == event.emojiName) {
                                exists = true
                                if (reaction.isSelected)
                                    reaction
                                else {
                                    commands {
                                        +ChatCommand.AddReaction(
                                            event.messageId,
                                            event.emojiName
                                        )
                                    }
                                    reaction.copy(count = reaction.count + 1, isSelected = true)
                                }
                            } else reaction
                        }.toMutableList()
                        if (!exists) {
                            commands {
                                +ChatCommand.AddReaction(event.messageId, event.emojiName)
                            }
                            reactions.add(
                                Reaction(event.emojiName, event.emojiCode, 1, true)
                            )
                        }
                        OwnMessageDelegateItem(
                            item.id(),
                            message.copy(reactions = reactions.toList())
                        )
                    }
                } else item
            }

            state { copy(items = newItems, positionToScroll = null) }
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
                                    commands {
                                        +ChatCommand.RemoveReaction(
                                            event.messageId,
                                            event.reaction.emojiName
                                        )
                                    }
                                    null
                                } else if (reaction.isSelected) {
                                    commands {
                                        +ChatCommand.RemoveReaction(
                                            event.messageId,
                                            event.reaction.emojiName
                                        )
                                    }
                                    reaction.copy(
                                        count = event.reaction.count - 1,
                                        isSelected = !event.reaction.isSelected
                                    )
                                } else {
                                    commands {
                                        +ChatCommand.AddReaction(
                                            event.messageId,
                                            event.reaction.emojiName
                                        )
                                    }
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
                                    commands {
                                        +ChatCommand.RemoveReaction(
                                            event.messageId,
                                            event.reaction.emojiName
                                        )
                                    }
                                    null
                                } else if (reaction.isSelected) {
                                    commands {
                                        +ChatCommand.RemoveReaction(
                                            event.messageId,
                                            event.reaction.emojiName
                                        )
                                    }
                                    reaction.copy(
                                        count = event.reaction.count - 1,
                                        isSelected = !event.reaction.isSelected
                                    )
                                } else {
                                    commands {
                                        +ChatCommand.AddReaction(
                                            event.messageId,
                                            event.reaction.emojiName
                                        )
                                    }
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
            state { copy(items = newItems, positionToScroll = null) }
        }
    }


    private fun Result.obtainNewMessages(items: List<ru.gozerov.tfs_spring.data.api.models.ZulipEvent>) {
        val messages = mutableListOf<ru.gozerov.tfs_spring.data.api.models.Message>()
        var lastId = -1
        items.forEach {
            lastId = it.id
            if (it.type == "message") {
                messages.add(it.message)
            }
        }
        var dateCount = 0
        var lastDate = ChannelsStub.lastDate
        val calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getTimeZone("Europe/Moscow")
        val messageItems = mutableListOf<DelegateItem>()
        messages.map { message ->
            val reactionCodes =
                mutableMapOf<String, ru.gozerov.tfs_spring.data.api.models.MutableReaction>()
            message.reactions.forEach { reaction ->
                val listR = reactionCodes[reaction.emoji_name]
                listR?.let {
                    it.count++
                    if (reaction.user_id == UserStub.CURRENT_USER_ID)
                        it.isSelected = true
                } ?: reactionCodes.put(
                    reaction.emoji_name,
                    ru.gozerov.tfs_spring.data.api.models.MutableReaction(
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
        state.items?.let {
            val msg = it + messageItems
            state { copy(items = msg, positionToScroll = msg.size - 1) }
            ru.gozerov.tfs_spring.data.api.EventQueueData.lastId = lastId
            ChannelsStub.lastDate = lastDate
            commands {
                +ChatCommand.GetEventsFromQueue(
                    ru.gozerov.tfs_spring.data.api.EventQueueData.queueId,
                    lastId
                )
            }
        }
    }

}