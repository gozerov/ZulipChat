package ru.gozerov.tfs_spring.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.core.DelegateItem
import ru.gozerov.tfs_spring.screens.chat.adapters.date.DateDelegateItem
import ru.gozerov.tfs_spring.screens.chat.adapters.date.DateModel
import ru.gozerov.tfs_spring.screens.chat.adapters.message.MessageDelegateItem
import ru.gozerov.tfs_spring.screens.chat.adapters.message.MessageModel
import ru.gozerov.tfs_spring.screens.chat.adapters.message.Reaction
import ru.gozerov.tfs_spring.screens.chat.adapters.user_message.UserMessageDelegateItem
import ru.gozerov.tfs_spring.screens.chat.adapters.user_message.UserMessageModel
import kotlin.random.Random

class ChatViewModel : ViewModel() {

    private val _messagesWithDate = MutableStateFlow<List<DelegateItem>>(emptyList())
    val messagesWithDate: StateFlow<List<DelegateItem>>
        get() = _messagesWithDate.asStateFlow()

    private val _reactions = MutableStateFlow<List<String>>(emptyList())
    val reactions: StateFlow<List<String>>
        get() = _reactions.asStateFlow()

    init {
        viewModelScope.launch {
            launch {
                val list = (0 until 4).map {
                    if (it % 2 == 0)
                        DateDelegateItem(it, DateModel(it, "1 Feb"))
                    else
                        MessageDelegateItem(
                            it,
                            MessageModel(
                                it,
                                "Author",
                                0,
                                "message",
                                listOf(Reaction("\uD83D\uDE00", Random.nextInt(1,5), Random.nextBoolean()))
                            )
                        )
                }
                _messagesWithDate.emit(list)
            }
            launch {
                val reactionList = listOf(
                    "\uD83D\uDE00",
                    "\uD83D\uDE06",
                    "\uD83D\uDE02",
                    "\uD83D\uDE03",
                    "\uD83D\uDE05",
                    "\uD83D\uDE08",
                    "\uD83D\uDE09",
                    "\uD83D\uDE10",
                    "\uD83D\uDE11",
                    "\uD83D\uDE16",
                    "\uD83D\uDE12",
                    "\uD83D\uDE13",
                    "\uD83D\uDE15",
                    "\uD83D\uDE18",
                    "\uD83D\uDE19"
                )
                _reactions.emit(reactionList)
            }
        }
    }

    fun updateReaction(messageId: Int, reaction: Reaction) {
        val message = _messagesWithDate.value.filterIsInstance<MessageDelegateItem>()
            .first { (it.content() as MessageModel).id == messageId }.content() as MessageModel
        val reactions = message.reactions.map { react ->
            if (react.emojiCode == reaction.emojiCode) {
                if (reaction.count != 0)
                    react.copy(count = reaction.count, isSelected = reaction.isSelected)
                else null
            } else
                react
        }
        val notNullReactions = reactions.filterNotNull()
        val newMessage = message.copy(reactions = notNullReactions)
        val newItems = _messagesWithDate.value.map {
            if (it is MessageDelegateItem && (it.content() as MessageModel).id == messageId)
                MessageDelegateItem(it.id, newMessage)
            else it
        }
        viewModelScope.launch {
            _messagesWithDate.emit(newItems)
        }
    }

    fun addReaction(messageId: Int, emojiCode: String) {
        val message = _messagesWithDate.value.filterIsInstance<MessageDelegateItem>()
            .first { (it.content() as MessageModel).id == messageId }.content() as MessageModel
        val currentReaction = message.reactions.firstOrNull { it.emojiCode == emojiCode }
        var exists = false
        val reaction = if (currentReaction != null) {
            exists = true
            if (currentReaction.isSelected)
                currentReaction
            else
                currentReaction.copy(count = currentReaction.count + 1, isSelected = true)
        } else
            Reaction(emojiCode, 1, true)
        val reactions =
            if (exists) message.reactions.map { if (it.emojiCode == emojiCode) reaction else it } else message.reactions + reaction
        val newMessage = message.copy(reactions = reactions)
        val newItems = _messagesWithDate.value.map {
            if (it is MessageDelegateItem && (it.content() as MessageModel).id == messageId)
                MessageDelegateItem(it.id, newMessage)
            else it
        }
        viewModelScope.launch {
            _messagesWithDate.emit(newItems)
        }
    }

    fun sendMessage(text: String) {
        val message = UserMessageModel(Random.nextInt(1000, 10000), text,  listOf(Reaction("\uD83D\uDE00", Random.nextInt(1,5
        ), Random.nextBoolean()), Reaction("\uD83D\uDE00", Random.nextInt(1,5
        ), Random.nextBoolean()), Reaction("\uD83D\uDE00", Random.nextInt(1,5
        ), Random.nextBoolean()), Reaction("\uD83D\uDE00", Random.nextInt(1,5
        ), Random.nextBoolean()), Reaction("\uD83D\uDE00", Random.nextInt(1,5
        ), Random.nextBoolean()), Reaction("\uD83D\uDE00", Random.nextInt(1,5
        ), Random.nextBoolean()))
        )
        val newItems = _messagesWithDate.value + UserMessageDelegateItem(_messagesWithDate.value.size, message)
        viewModelScope.launch {
            _messagesWithDate.emit(newItems)
        }
    }

}