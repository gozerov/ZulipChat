package ru.gozerov.tfs_spring.presentation.screens.channel.chat

import android.view.View
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher
import ru.gozerov.tfs_spring.R
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.ChatFragment

private var index = 0

object ChatFragmentScreen : KScreen<ChatFragmentScreen>() {

    override val layoutId: Int = R.layout.fragment_chat

    override val viewClass: Class<*> = ChatFragment::class.java

    val inputField = KEditText { withId(R.id.messageInputField) }

    val sendButton = KButton { withId(R.id.inputActionButton) }

    val recycler = KRecyclerView(
        builder = { withId(R.id.messageList) },
        itemTypeBuilder = {
            itemType(::KCardItem)
        }
    )

    class KCardItem(parent: Matcher<View>) : KRecyclerItem<KCardItem>(parent) {

        val content = KTextView {
            withIndex(index++) {
                withId(R.id.txtUserMessage)
            }
        }

    }


}