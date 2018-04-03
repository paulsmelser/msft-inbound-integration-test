package com.appdirect.integration.scenario.microsoft

import com.appdirect.integration.file.Resource.Companion.parseFile
import com.appdirect.jackson.json.Json.toJson
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.givenThat
import com.github.tomakehurst.wiremock.client.WireMock.urlMatching
import org.springframework.social.partnercenter.api.customer.Customer

object Customers {

    fun mockGetCustomer(customerId: String) {
        val customer = parseFile("data/customers/customer.json").getJsonAsObject<Customer>()
        customer.id = customerId
        customer.companyProfile.tenantId = customerId

        givenThat(get(urlMatching("/v1/customers/$customerId"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json; charset=utf-8")
                        .withBody(toJson(customer))))
    }
}