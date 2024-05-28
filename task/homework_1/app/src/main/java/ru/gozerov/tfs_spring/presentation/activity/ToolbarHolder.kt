package ru.gozerov.tfs_spring.presentation.activity

import androidx.fragment.app.Fragment
import kotlinx.coroutines.flow.StateFlow

interface ToolbarHolder {

    fun updateToolbar(toolbarState: ToolbarState)

    val searchFieldFlow: StateFlow<String>

}

fun Fragment.updateToolbar(toolbarState: ToolbarState) =
    (requireActivity() as ToolbarHolder).updateToolbar(toolbarState)

val Fragment.searchFieldFlow
    get() = (requireActivity() as ToolbarHolder).searchFieldFlow