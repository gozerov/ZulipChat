package ru.gozerov.tfs_spring.screens.channels.list.models

import android.graphics.Color
import ru.gozerov.tfs_spring.screens.channels.list.adapters.ChannelData

data class Topic(
    val id: Int,
    val channelId: Int,
    val color: Color,
    val title: String,
    val messageCount: Int
): ChannelData