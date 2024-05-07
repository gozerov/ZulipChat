package ru.gozerov.tfs_spring.data.remote.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ru.gozerov.tfs_spring.core.DelegateItem
import ru.gozerov.tfs_spring.core.utils.getEmojiByUnicode
import ru.gozerov.tfs_spring.core.utils.mapMonth
import ru.gozerov.tfs_spring.data.remote.api.ZulipApi
import ru.gozerov.tfs_spring.data.remote.api.models.Message
import ru.gozerov.tfs_spring.data.remote.api.models.MutableReaction
import ru.gozerov.tfs_spring.domain.stubs.UserStub
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.date.DateDelegateItem
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.date.DateModel
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.message.Reaction
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.message.own_message.OwnMessageDelegateItem
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.message.own_message.OwnMessageModel
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.message.user_message.UserMessageDelegateItem
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.message.user_message.UserMessageModel
import java.util.Calendar
import java.util.TimeZone

class MessagePagingSource @AssistedInject constructor(
    private val zulipApi: ZulipApi,
    @Assisted("stream") private val streamName: String,
    @Assisted("topic") private val topicName: String
) : PagingSource<Long, DelegateItem>() {

    private var lastId = INITIAL_PAGE_NUMBER
    private var _lastDate = ""

    override fun getRefreshKey(state: PagingState<Long, DelegateItem>): Long? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, DelegateItem> {
        val pageSize = params.loadSize

        val response = zulipApi.getMessages(
            pageSize, 0, lastId.toString(), "[\n" +
                    "    {\n" +
                    "        \"operator\": \"stream\",\n" +
                    "        \"operand\": \"$streamName\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"operator\": \"topic\",\n" +
                    "        \"operand\": \"$topicName\"\n" +
                    "    }\n" +
                    "]"
        )
        lastId = response.messages.firstOrNull()?.id?.toLong() ?: 0
        Log.e("AAAA", response.messages.size.toString())
        return LoadResult.Page(response.messages.map { UserMessageDelegateItem(it.id, UserMessageModel(it.id, it.sender_full_name, it.sender_id, it.content, listOf(),it.avatar_url)) }, null, null)
    }

    private fun mapMessages(messages: List<Message>): List<DelegateItem> {
        var dateCount = 0
        var lastDate = ""
        val calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getTimeZone("Europe/Moscow")
        val messageItems = mutableListOf<DelegateItem>()
        messages.mapIndexed { ind, message ->
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
            if (ind == 0)
                _lastDate = date
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
       // _lastDate = lastDate
        Log.e("AAAA", messageItems.size.toString())
        return messageItems
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("stream") streamName: String,
            @Assisted("topic") topicName: String
        ): MessagePagingSource

    }

    companion object {

        const val INITIAL_PAGE_NUMBER = 10000000000000000
    }

}