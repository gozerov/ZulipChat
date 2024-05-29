package ru.gozerov.tfs_spring.presentation.screens.channel.chat

import androidx.core.os.bundleOf
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.verify
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.gozerov.tfs_spring.R
import ru.gozerov.tfs_spring.di.DaggerTestAppComponent
import ru.gozerov.tfs_spring.di.DaggerTestChatComponent
import ru.gozerov.tfs_spring.di.TestAppComponent
import ru.gozerov.tfs_spring.mock.MockMessage
import ru.gozerov.tfs_spring.mock.MockMessage.Companion.message
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.ChatFragment
import ru.gozerov.tfs_spring.util.AppTestRule

@RunWith(AndroidJUnit4::class)
class ChatFragmentTest : TestCase() {

    @get:Rule
    val appRule = AppTestRule<ChatFragment> {}

    var appComponent: TestAppComponent? = null

    @Before
    fun setUp() {
        appComponent =
            DaggerTestAppComponent.factory().create(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun testChatFragment() = run {
        appRule.wiremockRule.message {
            withMessages()
            withSendEvent()
            withRegisteredEventQueue()
            withEvents()
            withDeleteQueueEvent()
            withUsers()
        }
        appRule.activityScenarioRule.scenario.onActivity { activity ->
            val fragment = ChatFragment()
            appComponent?.let {
                val testChatComponent =
                    DaggerTestChatComponent.factory().create(fragment.lifecycle, it)
                testChatComponent.inject(fragment)
                val fragmentArgs = bundleOf("channel" to "general", "topic" to "testing")
                fragment.arguments = fragmentArgs
                activity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.globalFragmentContainer, fragment)
                    .commitNow()
            }
        }
        run {
            ChatFragmentScreen {
                step("typing message") {
                    inputField.typeText("content")
                }
                step("click on send button") {
                    sendButton.click()
                }
                step("check calling /send method") {
                    verify(WireMock.postRequestedFor(MockMessage.messagesUrlPattern))
                }

                step("check new item in list") {
                    appRule.wiremockRule.message {
                        withMessageEvent()
                    }
                    appRule.wiremockRule.addMockServiceRequestListener { request, response ->
                        if (response.bodyAsString.contains("message")) {
                            appRule.wiremockRule.message {
                                withEvents()
                            }
                        }
                    }
                    flakySafely {
                        recycler {
                            childAt<ChatFragmentScreen.KCardItem>(0) {
                                content.hasText("content")
                            }
                        }
                    }
                }
            }
        }
    }

}