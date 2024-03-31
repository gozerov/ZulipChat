package ru.gozerov.tfs_spring.app

import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Cicerone

class TFSApp : Application() {

    private val cicerone = Cicerone.create()
    val router get() = cicerone.router
    val navigatorHolder get() = cicerone.getNavigatorHolder()

}

val Context.navigationHolder get() = (applicationContext as TFSApp).navigatorHolder
val Context.router get() = (applicationContext as TFSApp).router

val Fragment.navigationHolder get() = requireContext().navigationHolder
val Fragment.router get() = requireContext().router