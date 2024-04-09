package ru.gozerov.tfs_spring.activity

import androidx.fragment.app.Fragment
import kotlinx.coroutines.flow.SharedFlow

interface ToolbarHolder {

    fun updateToolbar(toolbarState: ToolbarState)

    val searchFieldFlow: SharedFlow<String>

}

fun Fragment.updateToolbar(toolbarState: ToolbarState) =
    (requireActivity() as ToolbarHolder).updateToolbar(toolbarState)

val Fragment.searchFieldFlow
    get() = (requireActivity() as ToolbarHolder).searchFieldFlow