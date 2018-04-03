package com.appdirect.integration.scenario

import com.appdirect.integration.file.Resource.Companion.parseFile
import com.appdirect.jackson.json.Json.toJson
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.givenThat
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import org.apache.http.HttpHeaders
import org.apache.http.entity.ContentType

object ISVEventMocks {

    fun mockSubscriptionOrderEventEndpoints(eventToken: String) {
        mockSubscriptionOrderFetch(eventToken)
        mockResolveEvent(eventToken)
    }
    private fun mockSubscriptionOrderFetch(eventToken: String) {
        val event = parseFile("data/isv-event/subscription_order.json").getAsObjectNode()
        event.put("id", eventToken)

        givenThat(get(urlPathEqualTo("/isv/event/$eventToken"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())
                        .withBody(parseFile(toJson(event)).flattenedText)))
    }

    private fun mockResolveEvent(eventToken: String) {
        givenThat(post(urlEqualTo("/api/integration/v1/events/$eventToken/result")))
    }
}