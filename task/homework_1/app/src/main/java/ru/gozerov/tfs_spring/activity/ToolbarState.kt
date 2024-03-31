package ru.gozerov.tfs_spring.activity

import androidx.annotation.ColorRes

sealed class ToolbarState {

    object None : ToolbarState()

    class OnlyStatusColor(
        @ColorRes val color: Int
    ): ToolbarState()

    object Search : ToolbarState()

    class NavUpWithTitle(
        @ColorRes val backgroundColor: Int,
        val title: String
    ) : ToolbarState()

}