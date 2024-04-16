package ru.gozerov.tfs_spring.screens.channels.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.core.DelegateItem
import ru.gozerov.core.runCatchingNonCancellation
import ru.gozerov.tfs_spring.api.ZulipApi
import ru.gozerov.tfs_spring.api.models.Message
import ru.gozerov.tfs_spring.api.models.MutableReaction
import ru.gozerov.tfs_spring.api.models.ZulipEvent
import ru.gozerov.tfs_spring.screens.channels.chat.adapters.date.DateDelegateItem
import ru.gozerov.tfs_spring.screens.channels.chat.adapters.date.DateModel
import ru.gozerov.tfs_spring.screens.channels.chat.adapters.message.MessageDelegateItem
import ru.gozerov.tfs_spring.screens.channels.chat.adapters.message.MessageModel
import ru.gozerov.tfs_spring.screens.channels.chat.adapters.message.Reaction
import ru.gozerov.tfs_spring.screens.channels.chat.adapters.user_message.UserMessageDelegateItem
import ru.gozerov.tfs_spring.screens.channels.chat.adapters.user_message.UserMessageModel
import ru.gozerov.tfs_spring.screens.channels.chat.models.ChatIntent
import ru.gozerov.tfs_spring.screens.channels.chat.models.ChatViewState
import ru.gozerov.tfs_spring.use_cases.AddReactionUseCase
import ru.gozerov.tfs_spring.use_cases.ChannelsStub
import ru.gozerov.tfs_spring.use_cases.DeleteEventQueueUseCase
import ru.gozerov.tfs_spring.use_cases.GetEventsFromQueueUseCase
import ru.gozerov.tfs_spring.use_cases.GetMessagesUseCase
import ru.gozerov.tfs_spring.use_cases.RegisterEventQueueUseCase
import ru.gozerov.tfs_spring.use_cases.RemoveReactionUseCase
import ru.gozerov.tfs_spring.use_cases.SendMessageUseCase
import ru.gozerov.tfs_spring.use_cases.UserStub
import ru.gozerov.tfs_spring.utils.getEmojiByUnicode
import ru.gozerov.tfs_spring.utils.mapMonth
import java.util.Calendar
import java.util.TimeZone

class ChatViewModel(
    private val zulipApi: ZulipApi
) : ViewModel() {

    private val _viewState = MutableStateFlow<ChatViewState>(ChatViewState.Empty)
    val viewState: StateFlow<ChatViewState>
        get() = _viewState.asStateFlow()

    private var _messagesWithDate: List<DelegateItem> = emptyList()

    private val _reactions = MutableStateFlow<List<String>>(emptyList())
    val reactions: StateFlow<List<String>>
        get() = _reactions.asStateFlow()

    private var queueId = ""
    private var lastEventId = -1

    init {
        viewModelScope.launch {
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

    fun handleIntent(intent: ChatIntent) {
        viewModelScope.launch {
            when (intent) {
                is ChatIntent.RegisterEventQueue -> {
                    runCatchingNonCancellation {
                        RegisterEventQueueUseCase(zulipApi).invoke(intent.topic)
                    }
                        .map {
                            lastEventId = it.last_event_id
                            queueId = it.queue_id
                            viewModelScope.launch {
                                val events =
                                    GetEventsFromQueueUseCase(zulipApi).invoke(
                                        queueId,
                                        lastEventId
                                    ).events
                                obtainNewMessages(events)
                            }
                        }
                }

                is ChatIntent.LoadMessages -> {
                    runCatchingNonCancellation {
                        GetMessagesUseCase(zulipApi).invoke(
                            intent.stream,
                            intent.topic
                        )
                    }
                        .map {
                            _messagesWithDate = it
                            _viewState.emit(
                                ChatViewState.LoadedChat(
                                    _messagesWithDate,
                                    _messagesWithDate.size - 1
                                )
                            )
                        }
                        .onFailure {
                            _viewState.emit(ChatViewState.Error())
                        }
                }

                is ChatIntent.UpdateReaction -> updateReaction(intent.messageId, intent.reaction)
                is ChatIntent.AddReaction -> addReaction(
                    intent.messageId,
                    intent.emojiName,
                    intent.emojiType,
                    intent.emojiCode
                )

                is ChatIntent.SendMessage ->
                    runCatchingNonCancellation {
                        SendMessageUseCase(zulipApi).invoke(
                            intent.channel,
                            intent.topic,
                            intent.content
                        )
                    }
                        .onFailure {
                            _viewState.emit(ChatViewState.Error())
                        }
            }
        }
    }

    //TODO: обработка реакции для отправленных пользователем сообщений

    private suspend fun updateReaction(messageId: Int, reaction: Reaction) {
        val index = _messagesWithDate.indexOfFirst {
            it is MessageDelegateItem && (it.content() as MessageModel).id == messageId
        }
        val message = _messagesWithDate[index].content() as MessageModel
        val reactions = message.reactions.map { react ->
            if (react.emojiCode == reaction.emojiCode) {
                if (reaction.count != 0) {
                    if (reaction.isSelected)
                        AddReactionUseCase(zulipApi).invoke(messageId, reaction.emojiName)
                    else
                        RemoveReactionUseCase(zulipApi).invoke(
                            messageId,
                            reaction.emojiName,
                            reaction.emojiType
                        )
                    react.copy(count = reaction.count, isSelected = reaction.isSelected)
                } else {
                    RemoveReactionUseCase(zulipApi).invoke(
                        messageId,
                        reaction.emojiName,
                        reaction.emojiType
                    )
                    null
                }
            } else
                react
        }
        val notNullReactions = reactions.filterNotNull()
        val newMessage = message.copy(reactions = notNullReactions)
        val newItems = _messagesWithDate.map {
            if (it is MessageDelegateItem && (it.content() as MessageModel).id == messageId)
                MessageDelegateItem(it.id, newMessage)
            else it
        }
        viewModelScope.launch {
            _messagesWithDate = newItems
            _viewState.emit(ChatViewState.LoadedChat(_messagesWithDate))
        }
    }

    private suspend fun addReaction(
        messageId: Int,
        emojiName: String,
        emojiType: String,
        emojiCode: String
    ) {
        val index = _messagesWithDate.indexOfFirst {
            it is MessageDelegateItem && (it.content() as MessageModel).id == messageId
        }
        val message = _messagesWithDate[index].content() as MessageModel
        val currentReaction = message.reactions.firstOrNull { it.emojiCode == emojiCode }
        var exists = false
        val reaction = if (currentReaction != null) {
            exists = true
            if (currentReaction.isSelected)
                currentReaction
            else {
                AddReactionUseCase(zulipApi).invoke(messageId, emojiName)
                currentReaction.copy(count = currentReaction.count + 1, isSelected = true)
            }
        } else {
            AddReactionUseCase(zulipApi).invoke(messageId, emojiName)
            Reaction(emojiName, emojiCode, emojiType, 1, true)
        }
        val reactions =
            if (exists) message.reactions.map { if (it.emojiCode == emojiCode) reaction else it } else message.reactions + reaction
        val newMessage = message.copy(reactions = reactions)
        val newItems = _messagesWithDate.map {
            if (it is MessageDelegateItem && (it.content() as MessageModel).id == messageId)
                MessageDelegateItem(it.id, newMessage)
            else it
        }
        viewModelScope.launch {
            _messagesWithDate = newItems
            _viewState.emit(ChatViewState.LoadedChat(_messagesWithDate))
        }
    }

    private suspend fun obtainNewMessages(items: List<ZulipEvent>) {
        val messages = mutableListOf<Message>()
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
            val reactionCodes = mutableMapOf<String, MutableReaction>()
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
                        false
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
                    it.type,
                    it.count,
                    it.isSelected
                )
            }
            if (message.sender_id == UserStub.CURRENT_USER_ID) {
                messageItems.add(
                    UserMessageDelegateItem(
                        message.id,
                        UserMessageModel(message.id, message.content, immutableReactions)
                    )
                )
            } else {
                messageItems.add(
                    MessageDelegateItem(
                        message.id,
                        MessageModel(
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
        _messagesWithDate = _messagesWithDate + messageItems
        _viewState.emit(ChatViewState.LoadedChat(_messagesWithDate, _messagesWithDate.size - 1))
        lastEventId = lastId
        ChannelsStub.lastDate = lastDate
        viewModelScope.launch {
            obtainNewMessages(GetEventsFromQueueUseCase(zulipApi).invoke(queueId, lastId).events)
        }
    }

    override fun onCleared() {
        ChannelsStub.lastDate = ""
        viewModelScope.launch {
            DeleteEventQueueUseCase(zulipApi).invoke(queueId)
        }
        super.onCleared()
    }

}