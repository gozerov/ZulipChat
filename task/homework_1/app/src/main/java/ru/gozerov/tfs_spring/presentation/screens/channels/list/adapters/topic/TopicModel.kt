package ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.topic

import android.graphics.Color

data class TopicModel(
    val id: Int,
    val channelId: Int,
    val color: Color,
    val title: String
)