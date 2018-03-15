package com.appdirect.integration.mock.microsoft

import com.appdirect.integration.file.Resource.Companion.parseFile
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.givenThat
import com.github.tomakehurst.wiremock.client.WireMock.urlMatching

object Customers {

    fun mockGetCustomer(customerId: String) {
        givenThat(get(urlMatching("/v1/customers/$customerId"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json; charset=utf-8")
                        .withBody(parseFile("data/customers/customer.json").flattenedText)))
    }
}