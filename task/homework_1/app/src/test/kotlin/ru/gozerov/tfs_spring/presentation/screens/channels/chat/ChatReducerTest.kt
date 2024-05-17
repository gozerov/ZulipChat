package ru.gozerov.tfs_spring.presentation.screens.channels.chat

import androidx.paging.PagingData
import io.kotest.matchers.collections.shouldContain
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import ru.gozerov.tfs_spring.core.DelegateItem
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.message.Reaction
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.message.user_message.UserMessageDelegateItem
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.message.user_message.UserMessageModel
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm.ChatReducer
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm.models.ChatCommand
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm.models.ChatEffect
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm.models.ChatEvent
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm.models.ChatState

@RunWith(JUnit4::class)
class ChatReducerTest {

    @Test
    fun `reduce initial event`() {
        //Given
        val reducer = ChatReducer()
        val state = ChatState()
        val stream = "general"
        val topic = "topic"
        val fromCache = true
        val event = ChatEvent.UI.Init(stream, topic, fromCache)
        //When
        val actual = reducer.reduce(event, state)
        //Then
        assertTrue(actual.state.isLoading)
        actual.commands.shouldContain(ChatCommand.LoadChat(stream, topic, fromCache))
        actual.commands.shouldContain(ChatCommand.RegisterEventQueue(topic))
    }

    @Test
    fun `reduce add reaction event`() {
        //Given
        val reducer = ChatReducer()
        val messageId = 1
        val emojiName = "emojiName"
        val emojiCode = "emojiCode"
        val state = ChatState()
        val event = ChatEvent.UI.AddReaction(messageId, emojiName, emojiCode)
        //When
        val actual = reducer.reduce(event, state)
        //Then
        actual.commands.shouldContain(ChatCommand.AddReaction(messageId, emojiName))
    }

    @Test
    fun `reduce update reaction event`() {
        //Given
        val reducer = ChatReducer()
        val messageId = 1
        val emojiName = "emojiName"
        val emojiCode = "emojiCode"
        val count = 1
        val isSelected = false
        val reaction = Reaction(emojiName, emojiCode, count, isSelected)
        val state = ChatState()
        val event = ChatEvent.UI.UpdateReaction(messageId, reaction)
        //When
        val actual = reducer.reduce(event, state)
        //Then
        if (!isSelected)
            actual.commands.shouldContain(ChatCommand.AddReaction(messageId, emojiName))
        else
            actual.commands.shouldContain(ChatCommand.RemoveReaction(messageId, emojiName))
    }

    @Test
    fun `reduce load chat success event`() {
        //Given
        val reducer = ChatReducer()
        val state = ChatState()
        val data = PagingData.from(listOf<DelegateItem>(UserMessageDelegateItem(0, UserMessageModel(0, "", 1, "", listOf(), null))))
        val items = flowOf(data)
        val event = ChatEvent.Internal.LoadChatSuccess(items, false)
        //When
        val actual = reducer.reduce(event, state)
        //Then
        val expectedState = ChatState(isLoading = false, fromCache = false, flowItems = actual.state.flowItems, items = null)
        assertEquals(expectedState, actual.state)
    }

    @Test
    fun `reduce load chat error event`() {
        //Given
        val reducer = ChatReducer()
        val state = ChatState()
        val event = ChatEvent.Internal.LoadChatError
        //When
        val actual = reducer.reduce(event, state)
        //Then
        val expectedState = state.copy(isLoading = false)
        assertEquals(expectedState, actual.state)
        assertTrue(actual.effects.contains(ChatEffect.ShowError))
    }

    @Test
    fun `reduce load messages event`() {
        //Given
        val reducer = ChatReducer()
        val state = ChatState()
        val stream = "stream"
        val topic = "topic"
        val fromCache = false
        val event = ChatEvent.UI.LoadMessages(stream, topic, fromCache)
        //When
        val actual = reducer.reduce(event, state)
        //Then
        val expectedState = state.copy(isLoading = true)
        assertEquals(expectedState, actual.state)
        assertTrue(actual.commands.contains(ChatCommand.LoadChat(stream, topic, fromCache)))
    }

    @Test
    fun `reduce save messages event`() {
        //Given
        val reducer = ChatReducer()
        val state = ChatState()
        val data = PagingData.from(listOf<DelegateItem>(UserMessageDelegateItem(0, UserMessageModel(0, "", 1, "", listOf(), null))))
        val event = ChatEvent.UI.SaveMessages(data)
        //When
        val actual = reducer.reduce(event, state)
        //Then
        val expectedState = ChatState(items = data)
        assertEquals(expectedState, actual.state)
    }

    @Test
    fun `reduce send message event`() {
        //Given
        val reducer = ChatReducer()
        val state = ChatState()
        val channel = "channel"
        val topic = "topic"
        val content = "content"
        val event = ChatEvent.UI.SendMessage(channel, topic, content)
        //When
        val actual = reducer.reduce(event, state)
        //Then
        assertTrue(actual.commands.contains(ChatCommand.SendMessage(channel, topic, content)))
    }

    @Test
    fun `reduce register event queue event`() {
        //Given
        val reducer = ChatReducer()
        val state = ChatState()
        val topic = "topic"
        val event = ChatEvent.UI.RegisterEventQueue(topic)
        //When
        val actual = reducer.reduce(event, state)
        //Then
        assertTrue(actual.commands.contains(ChatCommand.RegisterEventQueue(event.topic)))
    }

    @Test
    fun `reduce registered event queue event`() {
        //Given
        val reducer = ChatReducer()
        val state = ChatState()
        val queueId = "queue_id"
        val lastEventId = -1
        val event = ChatEvent.Internal.RegisteredEventQueue(queueId, lastEventId)
        //When
        val actual = reducer.reduce(event, state)
        //Then
        assertTrue(actual.commands.contains(ChatCommand.GetEventsFromQueue(queueId, lastEventId)))
    }

    @Test
    fun `reduce exit event`() {
        //Given
        val reducer = ChatReducer()
        val state = ChatState()
        val event = ChatEvent.UI.Exit
        //When
        val actual = reducer.reduce(event, state)
        //Then
        assertTrue(actual.commands.contains(ChatCommand.Exit))
    }

}