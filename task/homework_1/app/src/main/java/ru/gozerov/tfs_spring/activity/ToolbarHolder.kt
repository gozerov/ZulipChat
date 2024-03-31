package ru.gozerov.tfs_spring.activity

import androidx.fragment.app.Fragment

interface ToolbarHolder {

    fun updateToolbar(toolbarState: ToolbarState)

}

fun Fragment.updateToolbar(toolbarState: ToolbarState) =
    (requireActivity() as ToolbarHolder).updateToolbar(toolbarState)