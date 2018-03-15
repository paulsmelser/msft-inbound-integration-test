package com.appdirect.integration.mock

import com.appdirect.integration.file.Resource.Companion.parseFile
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.givenThat
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import org.apache.http.HttpHeaders
import org.apache.http.entity.ContentType

object ISVEventMocks {

    fun mockEventEndpoints(scenarioId: String, eventToken: String) {
        mockFetchEvent(scenarioId, eventToken)
        mockResolveEvent(scenarioId, eventToken)
    }
    fun mockFetchEvent(scenarioId: String, eventToken: String) {
        givenThat(get(urlPathEqualTo("/isv/event/$eventToken"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())
                        .withBody(parseFile("data/isv-event/$scenarioId/get.json").flattenedText)))
    }

    fun mockResolveEvent(scenarioId: String, eventToken: String) {
        givenThat(post(urlEqualTo("/api/integration/v1/events/$eventToken/result")))
    }
}