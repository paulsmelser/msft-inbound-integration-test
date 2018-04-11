package com.appdirect.integration.scenario.mocks.microsoft

import com.appdirect.integration.file.Resource.Companion.parseFile
import com.appdirect.integration.scenario.ScenarioContext
import com.appdirect.jackson.json.Json
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.givenThat
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import org.springframework.social.partnercenter.api.order.Order

object OrderMocks {
    fun mockOrderCreationWithSuccessfulProvisioning(scenarioContext: ScenarioContext) {
        mockPlaceOrder(scenarioContext.customerId, scenarioContext.orderId, scenarioContext.subscriptionId)
        mockProvisioningStatus(scenarioContext.customerId, scenarioContext.subscriptionId)
    }
    fun mockPlaceOrder(tenantId: String, orderId: String, subscriptionId: String){
        val order = parseFile("data/order/create.json").getJsonAsObject<Order>()
        order.referenceCustomerId = tenantId
        order.id = orderId
        order.lineItems[0].subscriptionId = subscriptionId

        givenThat(post(urlEqualTo("/v1/customers/$tenantId/orders"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json; charset=utf-8")
                        .withBody(Json.toJson(order))))
    }

    fun mockProvisioningStatus(tenantId: String, subscriptionId: String) {
        givenThat(get(urlEqualTo("/v1/customers/$tenantId/subscriptions/$subscriptionId/provisioningstatus"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json; charset=utf-8")
                        .withBody(parseFile("data/order/provisioning/success.json").flattenedText)))
    }
}