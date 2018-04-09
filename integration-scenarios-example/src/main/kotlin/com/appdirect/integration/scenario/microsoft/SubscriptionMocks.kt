package com.appdirect.integration.scenario.microsoft

import com.appdirect.integration.file.Resource
import com.appdirect.jackson.json.Json
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.givenThat
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import org.springframework.social.partnercenter.api.order.subscription.Subscription

object SubscriptionMocks {
    fun mockGetSubscription(tenantId: String, subscriptionId: String, orderId: String) {
        val subscription = Resource.parseFile("data/subscription/ok.json").getJsonAsObject<Subscription>()
        subscription.id = subscriptionId
        subscription.orderId = orderId

        givenThat(get(urlEqualTo("/v1/customers/$tenantId/subscriptions/$subscriptionId"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json; charset=utf-8")
                        .withBody(Json.toJson(subscription))))
    }
}