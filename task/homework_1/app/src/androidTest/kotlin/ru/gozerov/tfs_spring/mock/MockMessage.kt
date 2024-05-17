package ru.gozerov.tfs_spring.mock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching
import ru.gozerov.tfs_spring.util.AssetsUtils.fromAssets


class MockMessage(
    private val wireMockServer: WireMockServer
) {

    private val matcher = WireMock.get(urlPattern)

    fun withMessages() {
        wireMockServer.stubFor(matcher.willReturn(ok(fromAssets("message/messages.json"))))
    }

    fun withEmptyList() {
        wireMockServer.stubFor(matcher.willReturn(ok("[]")))
    }

    companion object {

        val urlPattern = urlPathMatching("/message/.+")

        fun WireMockServer.message(block: MockMessage.() -> Unit) {
            MockMessage(this).apply(block)
        }

    }

}