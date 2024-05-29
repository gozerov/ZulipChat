package ru.gozerov.tfs_spring.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ru.gozerov.tfs_spring.core.DelegateItem
import ru.gozerov.tfs_spring.core.utils.getEmojiByUnicode
import ru.gozerov.tfs_spring.core.utils.mapMonth
import ru.gozerov.tfs_spring.data.cache.dao.MessageDao
import ru.gozerov.tfs_spring.data.cache.entities.toMessage
import ru.gozerov.tfs_spring.data.cache.entities.toMessageEntity
import ru.gozerov.tfs_spring.data.cache.storage.AppStorage
import ru.gozerov.tfs_spring.data.remote.api.ZulipApi
import ru.gozerov.tfs_spring.data.remote.api.models.Message
import ru.gozerov.tfs_spring.data.remote.api.models.MutableReaction
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
    private val messageDao: MessageDao,
    private val appStorage: AppStorage,
    @Assisted("stream") private val streamName: String,
    @Assisted("topic") private val topicName: String,
    @Assisted("fromCache") private val fromCache: Boolean
) : PagingSource<Int, DelegateItem>() {

    private var lastId = INITIAL_PAGE_NUMBER
    private var _lastDate = ""
    private var loadedFromCache: Boolean = false

    override fun getRefreshKey(state: PagingState<Int, DelegateItem>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DelegateItem> {
        val page = params.key ?: 1
        val pageSize = params.loadSize
        if (params.key == null && fromCache) {
            val savedMessages = messageDao.getMessages(streamName, topicName)
            if (savedMessages.isNotEmpty()) {
                loadedFromCache = true
                return LoadResult.Page(
                    mapMessages(savedMessages.map { it.toMessage(listOf()) }),
                    0,
                    null,
                    0,
                    0
                )
            }
        }
        try {
            val id = appStorage.getUserId()

            if (id == -1) {
                val userId = zulipApi.getOwnUser().user_id
                appStorage.saveUserId(userId)
            }

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
            val prevKey = if (response.messages.size < 20) null else page + 1
            val nextKey = if (page == 1) null else page - 1
            lastId = response.messages.firstOrNull()?.id?.toLong() ?: 0
            if (page == 1) {
                messageDao.clear(streamName, topicName)
                messageDao.saveMessages(response.messages.map {
                    it.toMessageEntity(
                        streamName,
                        topicName
                    )
                })
                return LoadResult.Page(mapMessages(response.messages), prevKey, nextKey, 0, 0)
            }

            return LoadResult.Page(mapMessages(response.messages), prevKey, nextKey)
        } catch (e: Exception) {
            return LoadResult.Error<Int, DelegateItem>(e)
        }
    }

    private fun mapMessages(messages: List<Message>): List<DelegateItem> {
        val userId = appStorage.getUserId()
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
                    if (reaction.user_id == userId)
                        it.isSelected = true
                } ?: reactionCodes.put(
                    reaction.emoji_name,
                    MutableReaction(
                        reaction.emoji_name,
                        reaction.reaction_type,
                        reaction.emoji_code,
                        1,
                        reaction.user_id == userId
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
            if (date != lastDate && date != _lastDate) {
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
            if (message.sender_id == userId) {
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
        _lastDate = lastDate
        return messageItems
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("stream") streamName: String,
            @Assisted("topic") topicName: String,
            @Assisted("fromCache") fromCache: Boolean
        ): MessagePagingSource

    }

    companion object {

        const val INITIAL_PAGE_NUMBER = 10000000000000000
    }

}