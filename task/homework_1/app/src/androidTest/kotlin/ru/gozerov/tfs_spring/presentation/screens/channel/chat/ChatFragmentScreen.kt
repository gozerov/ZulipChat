package ru.gozerov.tfs_spring.presentation.screens.channel.chat

import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.edit.KEditText
import ru.gozerov.tfs_spring.R
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.ChatFragment

object ChatFragmentScreen : KScreen<ChatFragmentScreen>() {

    override val layoutId: Int = R.layout.fragment_chat

    override val viewClass: Class<*> = ChatFragment::class.java

    val inputField = KEditText { withId(R.id.messageInputField) }

}