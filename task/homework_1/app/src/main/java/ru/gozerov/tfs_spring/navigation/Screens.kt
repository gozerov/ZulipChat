package ru.gozerov.tfs_spring.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.gozerov.tfs_spring.screens.chat.ChatFragment
import ru.gozerov.tfs_spring.screens.contacts.ContactsFragment
import ru.gozerov.tfs_spring.screens.profile.ProfileFragment
import ru.gozerov.tfs_spring.screens.tabs.TabsFragment

object Screens {

    fun Tabs() = FragmentScreen { TabsFragment() }
    fun Channels() = FragmentScreen("1") { ChatFragment() }
    fun Contacts() = FragmentScreen("2") { ContactsFragment() }
    fun Profile() = FragmentScreen("3") { ProfileFragment() }

}