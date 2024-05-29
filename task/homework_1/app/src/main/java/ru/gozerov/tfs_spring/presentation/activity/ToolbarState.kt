package ru.gozerov.tfs_spring.presentation.activity

import androidx.annotation.ColorRes

sealed class ToolbarState {

    object None : ToolbarState()

    class OnlyStatusColor(
        @ColorRes val color: Int
    ) : ToolbarState()

    class Search(
        val title: String,
        val isFocused: Boolean = false
    ) : ToolbarState()


    class SearchWithText(
        val text: String
    ) : ToolbarState()

    class NavUpWithTitle(
        @ColorRes val backgroundColor: Int,
        val title: String,
        val gravity: TitleGravity = TitleGravity.START
    ) : ToolbarState()

}