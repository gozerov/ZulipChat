package ru.gozerov.tfs_spring.domain.use_cases

import android.graphics.Color
import androidx.core.graphics.toColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import ru.gozerov.tfs_spring.domain.repositories.ZulipRepository
import ru.gozerov.tfs_spring.domain.stubs.ChannelsStub
import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.channel.ChannelDelegateItem
import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.channel.ChannelModel
import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.topic.TopicDelegateItem
import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.topic.TopicModel
import javax.inject.Inject

class GetChannelsUseCase @Inject constructor(
    private val zulipRepository: ZulipRepository
) {

    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        return@withContext flow {
            val streamFlow = zulipRepository.getStreams()
            streamFlow.collect { streamMap ->
                val subscribedStreams = streamMap[ChannelsStub.categories[0]]!!
                val allStreams = streamMap[ChannelsStub.categories[1]]!!
                val allTopics = allStreams.map { it.topics }
                val subscribedTopics = subscribedStreams.map { it.topics }
                val channels = listOf(subscribedStreams.mapIndexed { index, fullStream ->
                    val stream = fullStream.stream
                    ChannelDelegateItem(
                        stream.stream_id,
                        ChannelModel(
                            stream.stream_id,
                            stream.name,
                            subscribedTopics[index].map {
                                TopicDelegateItem(
                                    it.max_id,
                                    TopicModel(
                                        it.max_id,
                                        stream.stream_id,
                                        Color.DKGRAY.toColor(),
                                        it.name,
                                        0
                                    )
                                )
                            })
                    )
                }, allStreams.mapIndexed { index, fullStream ->
                    val stream = fullStream.stream
                    ChannelDelegateItem(
                        stream.stream_id,
                        ChannelModel(
                            stream.stream_id,
                            stream.name,
                            allTopics[index].map {
                                TopicDelegateItem(
                                    it.max_id,
                                    TopicModel(
                                        it.max_id,
                                        stream.stream_id,
                                        Color.DKGRAY.toColor(),
                                        it.name,
                                        0
                                    )
                                )
                            })
                    )
                })
                ChannelsStub.originalAllCombinedChannels =
                    ChannelsStub.categories.associateWith {
                        channels[ChannelsStub.categories.indexOf(
                            it
                        )]
                    }
                ChannelsStub.allCombinedChannels = ChannelsStub.originalAllCombinedChannels
                emit(ChannelsStub.allCombinedChannels)
            }
        }

    }

}