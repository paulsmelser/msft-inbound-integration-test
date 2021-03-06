package com.appdirect.integration.scenario.mocks.microsoft

import com.appdirect.integration.file.Resource.Companion.parseFile
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.givenThat
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.urlMatching

object UserMocks {

    fun mockCreateUser(tenantId: String) {
        givenThat(post(urlEqualTo("/v1/customers/$tenantId/users"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json; charset=utf-8")
                        .withBody(parseFile("data/users/create.json").flattenedText)))
    }
    fun mockAssignUser(tenantId: String) {
        givenThat(post(urlMatching("/v1/customers/$tenantId/users/.*/licenseupdates"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json; charset=utf-8")
                        .withBody(parseFile("data/users/assign.json").flattenedText)))
    }
}