package ru.gozerov.tfs_spring.mock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching
import ru.gozerov.tfs_spring.util.AssetsUtils.fromAssets


class MockMessage(
    private val wireMockServer: WireMockServer
) {

    private val messagesMatcher = WireMock.get(messagesUrlPattern)
    private val sendMessageMatcher = WireMock.post(messagesUrlPattern)
    private val registerEventQueueMatcher = WireMock.post(eventQueueUrlPattern)
    private val eventsMatcher = WireMock.get(eventsPattern)
    private val deleteEventQueueMatcher = WireMock.delete(eventsPattern)

    fun withMessages() {
        wireMockServer.stubFor(messagesMatcher.willReturn(ok(fromAssets("messages/messages.json"))))
    }

    fun withEvents() {
        wireMockServer.stubFor(eventsMatcher.willReturn(ok(fromAssets("messages/heartbeat.json"))))
    }

    fun withSendEvent() {
        wireMockServer.stubFor(sendMessageMatcher.willReturn(ok()))
    }

    fun withDeleteQueueEvent() {
        wireMockServer.stubFor(deleteEventQueueMatcher.willReturn(ok()))
    }

    fun withMessageEvent() {
        wireMockServer.stubFor(eventsMatcher.willReturn(ok(fromAssets("messages/messageEvent.json"))))
    }

    fun withRegisteredEventQueue() {
        wireMockServer.stubFor(registerEventQueueMatcher.willReturn(ok(fromAssets("messages/registeredEventQueue.json"))))
    }

    companion object {

        val messagesUrlPattern = urlPathMatching("/messages")
        val eventQueueUrlPattern = urlPathMatching("/register")
        val eventsPattern = urlPathMatching("/events")

        fun WireMockServer.message(block: MockMessage.() -> Unit) {
            MockMessage(this).apply(block)
        }

    }

}