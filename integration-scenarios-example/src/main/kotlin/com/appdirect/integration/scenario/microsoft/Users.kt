package com.appdirect.integration.scenario.microsoft

import com.appdirect.integration.file.Resource.Companion.parseFile
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.givenThat
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.urlMatching

object Users {

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