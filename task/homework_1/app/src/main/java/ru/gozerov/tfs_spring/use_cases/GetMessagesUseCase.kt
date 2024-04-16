package ru.gozerov.tfs_spring.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.core.DelegateItem
import ru.gozerov.tfs_spring.api.ZulipApi
import ru.gozerov.tfs_spring.api.models.MutableReaction
import ru.gozerov.tfs_spring.screens.channels.chat.adapters.date.DateDelegateItem
import ru.gozerov.tfs_spring.screens.channels.chat.adapters.date.DateModel
import ru.gozerov.tfs_spring.screens.channels.chat.adapters.message.MessageDelegateItem
import ru.gozerov.tfs_spring.screens.channels.chat.adapters.message.MessageModel
import ru.gozerov.tfs_spring.screens.channels.chat.adapters.message.Reaction
import ru.gozerov.tfs_spring.screens.channels.chat.adapters.user_message.UserMessageDelegateItem
import ru.gozerov.tfs_spring.screens.channels.chat.adapters.user_message.UserMessageModel
import ru.gozerov.tfs_spring.utils.mapMonth
import java.util.Calendar
import java.util.TimeZone

class GetMessagesUseCase(
    private val zulipApi: ZulipApi
) {

    suspend operator fun invoke(stream: String, topic: String) = withContext(Dispatchers.IO) {
        val messages = zulipApi.getMessages(
            100, 100, "newest", "[\n" +
                    "    {\n" +
                    "        \"operator\": \"stream\",\n" +
                    "        \"operand\": \"$stream\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"operator\": \"topic\",\n" +
                    "        \"operand\": \"$topic\"\n" +
                    "    }\n" +
                    "]"
        ).messages
        var dateCount = 0
        var lastDate = ""
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
                    MutableReaction(reaction.emoji_name, reaction.reaction_type, reaction.emoji_code, 1, false)
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
        ChannelsStub.lastDate = lastDate
        return@withContext messageItems
    }

    private fun getEmojiByUnicode(reactionCode: String, type: String): String {
        return if (type == "unicode_emoji") {
            if (reactionCode.contains('-')) {

                val points = reactionCode.split('-').map { it.toInt(16) }.toIntArray()
                String(points, 0, points.size)
            } else String(Character.toChars(reactionCode.toInt(16)))
        } else reactionCode
    }

}