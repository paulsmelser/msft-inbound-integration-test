package com.appdirect.integration.scenario.mocks.microsoft

import com.appdirect.integration.file.Resource.Companion.parseFile
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.givenThat
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.urlMatching
import com.github.tomakehurst.wiremock.http.HttpHeader.httpHeader
import com.github.tomakehurst.wiremock.http.HttpHeaders
import java.util.UUID

object MicrosoftOAuthMocks {
    fun mockMicrosoftOAuthEndpoints() {
        oauthLoginMock()
        generateTokenMock()
    }
    private fun oauthLoginMock() {
        givenThat(post(urlMatching(".*.onmicrosoft.com/oauth2/token"))
                .willReturn(aResponse()
                        .withHeaders(HttpHeaders(
                                httpHeader("Content-Type", "application/json;charset=utf-8"),
                                httpHeader("X-Content-Type-Options", "nosniff"),
                                httpHeader("x-ms-request-id", UUID.randomUUID().toString())
                        ))
                        .withBody(parseFile("data/oauth/token.json").flattenedText)))
    }

    private fun generateTokenMock() {
        givenThat(post(urlEqualTo("/generatetoken"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBody(parseFile("data/oauth/app_token.json").flattenedText)))
    }
}