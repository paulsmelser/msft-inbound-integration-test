package com.appdirect.integration.scenario.microsoft

import com.appdirect.integration.file.Resource.Companion.parseFile
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.givenThat
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo

object Orders {
    fun mockOrderCreationWithSuccessfulProvisioning(tenantId: String) {
        mockPlaceOrder(tenantId)
        mockProvisioningStatus(tenantId)
    }
    fun mockPlaceOrder(tenantId: String){
        givenThat(post(urlEqualTo("/v1/customers/$tenantId/orders"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json; charset=utf-8")
                        .withBody(parseFile("data/order/create.json").flattenedText)))
    }

    fun mockProvisioningStatus(tenantId: String) {
        givenThat(get(urlEqualTo("/v1/customers/$tenantId/subscriptions/84A03D81-6B37-4D66-8D4A-FAEA24541538/provisioningstatus"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json; charset=utf-8")
                        .withBody(parseFile("data/order/provisioning/success.json").flattenedText)))
    }
}