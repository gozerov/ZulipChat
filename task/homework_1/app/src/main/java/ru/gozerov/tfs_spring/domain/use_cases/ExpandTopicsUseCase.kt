package ru.gozerov.tfs_spring.domain.use_cases

import android.graphics.Color
import androidx.core.graphics.toColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import ru.gozerov.tfs_spring.core.DelegateItem
import ru.gozerov.tfs_spring.domain.models.ExpandTopicResult
import ru.gozerov.tfs_spring.domain.repositories.ZulipRepository
import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.channel.ChannelDelegateItem
import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.channel.ChannelModel
import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.topic.TopicDelegateItem
import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.topic.TopicModel
import javax.inject.Inject

class ExpandTopicsUseCase @Inject constructor(
    private val zulipRepository: ZulipRepository
) {

    suspend operator fun invoke(
        channel: ChannelModel,
        categoryPosition: Int
    ): ExpandTopicResult = withContext(Dispatchers.IO) {
        async {
            val streams = zulipRepository.getCachedStreams(categoryPosition)

            val expandedIds = zulipRepository.getExpandedStreams(categoryPosition)
            val newList = mutableListOf<DelegateItem>()

            streams.second.forEach { stream ->
                if (stream.stream_id == channel.id) {
                    val isExpanded = stream.stream_id in expandedIds
                    newList.add(
                        ChannelDelegateItem(
                            stream.stream_id,
                            channel.copy(isExpanded = !isExpanded)
                        )
                    )
                    zulipRepository.setStreamExpand(categoryPosition, stream.stream_id, isExpanded)
                    if (!isExpanded) {
                        newList.addAll(
                            zulipRepository.getTopics(stream.stream_id).map { topic ->
                                TopicDelegateItem(
                                    topic.max_id, TopicModel(
                                        topic.max_id,
                                        stream.stream_id,
                                        Color.DKGRAY.toColor(),
                                        topic.name,
                                    )
                                )
                            }
                        )
                    }
                } else
                    newList.add(
                        ChannelDelegateItem(
                            stream.stream_id,
                            ChannelModel(stream.stream_id, stream.name, listOf())
                        )
                    )
            }
            return@async ExpandTopicResult(streams.first, newList)
        }.await()
    }

}