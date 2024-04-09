package ru.gozerov.tfs_spring.use_cases

import android.graphics.Color
import androidx.core.graphics.toColor
import ru.gozerov.core.DelegateItem
import ru.gozerov.tfs_spring.screens.channels.list.adapters.channel.ChannelDelegateItem
import ru.gozerov.tfs_spring.screens.channels.list.adapters.channel.ChannelModel
import ru.gozerov.tfs_spring.screens.channels.list.adapters.topic.TopicDelegateItem
import ru.gozerov.tfs_spring.screens.channels.list.adapters.topic.TopicModel

object ChannelsStub {

    val categories = listOf("Subscribed", "All streams")

    private val originalChannels = listOf<DelegateItem>(
        ChannelDelegateItem(
            0, ChannelModel(
                0,
                "#general",
                listOf(
                    TopicDelegateItem(
                        0,
                        TopicModel(
                            1,
                            0,
                            Color.parseColor("#FF2A9D8F").toColor(),
                            "Testing",
                            1240
                        )
                    ),
                    TopicDelegateItem(
                        1,
                        TopicModel(
                            2,
                            0,
                            Color.parseColor("#FFE9C46A").toColor(),
                            "Bruh",
                            24
                        )
                    )
                )
            )
        ),

        ChannelDelegateItem(
            1, ChannelModel(
                3,
                "#Development",
                listOf()
            )
        ),
        ChannelDelegateItem(
            2,
            ChannelModel(
                6,
                "#general",
                listOf(
                    TopicDelegateItem(
                        2,
                        TopicModel(
                            3,
                            6,
                            Color.parseColor("#FF2A9D8F").toColor(),
                            "Testing",
                            1240
                        )
                    ),
                    TopicDelegateItem(
                        3,
                        TopicModel(
                            4,
                            6,
                            Color.parseColor("#FFE9C46A").toColor(),
                            "Bruh",
                            24
                        )
                    )
                )
            )
        )
    )

    val originalCombinedChannels = categories.associateWith { originalChannels }
    var combinedChannels = originalCombinedChannels


}