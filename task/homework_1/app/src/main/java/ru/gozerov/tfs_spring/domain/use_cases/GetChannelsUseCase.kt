package ru.gozerov.tfs_spring.domain.use_cases

import android.graphics.Color
import androidx.core.graphics.toColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.tfs_spring.domain.stubs.ChannelsStub
import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.channel.ChannelDelegateItem
import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.channel.ChannelModel
import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.topic.TopicDelegateItem
import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.topic.TopicModel

class GetChannelsUseCase(
    private val zulipApi: ru.gozerov.tfs_spring.data.api.ZulipApi
) {

    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        val allStreams = zulipApi.getAllStreams()
        val allTopics = allStreams.streams.map {
            zulipApi.getStreamTopics(it.stream_id)
        }
        val subscribedStreams = zulipApi.getSubscribedStreams()
        val subscribedTopics = subscribedStreams.subscriptions.map {
            zulipApi.getStreamTopics(it.stream_id)
        }
        val channels = listOf(subscribedStreams.subscriptions.mapIndexed { index, stream ->
            ChannelDelegateItem(
                stream.stream_id,
                ChannelModel(
                    stream.stream_id,
                    stream.name,
                    subscribedTopics[index].topics.map {
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
        }, allStreams.streams.mapIndexed { index, stream ->
            ChannelDelegateItem(
                stream.stream_id,
                ChannelModel(
                    stream.stream_id,
                    stream.name,
                    allTopics[index].topics.map {
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
            ChannelsStub.categories.associateWith { channels[ChannelsStub.categories.indexOf(it)] }
        ChannelsStub.allCombinedChannels = ChannelsStub.originalAllCombinedChannels
        return@withContext ChannelsStub.allCombinedChannels
    }

}