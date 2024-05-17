package ru.gozerov.tfs_spring.presentation.screens.channel.chat

import androidx.core.os.bundleOf
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Test
import org.junit.runner.RunWith
import ru.gozerov.tfs_spring.R
import ru.gozerov.tfs_spring.presentation.activity.MainActivity
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.ChatFragment

@RunWith(AndroidJUnit4::class)
class ChatFragmentTest : TestCase() {

    @Test
    fun testChatFragment() = run {
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        scenario.onActivity { activity ->
            val fragment = ChatFragment()
            val fragmentArgs = bundleOf("channel" to "channel", "topic" to "topic")
            fragment.arguments = fragmentArgs
            activity.supportFragmentManager
                .beginTransaction()
                .replace(R.id.globalFragmentContainer, fragment)
                .commitNow()
        }
        run {
            step("checking inputField visibility") {
                onView(withId(R.id.messageInputField)).check(matches(isDisplayed()))
            }
        }
    }

}