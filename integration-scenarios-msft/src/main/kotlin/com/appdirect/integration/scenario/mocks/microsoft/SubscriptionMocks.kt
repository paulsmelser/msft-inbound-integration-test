package com.appdirect.integration.scenario.mocks.microsoft

import com.appdirect.integration.file.Resource
import com.appdirect.integration.scenario.Scenario
import com.appdirect.integration.scenario.ScenarioContext
import com.psmelser.jackson.json.Json
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.givenThat
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import org.springframework.social.partnercenter.api.order.subscription.Subscription

object SubscriptionMocks {

    fun getSubscriptionMock(scenario: Scenario<ScenarioContext>) {
        val subscription = Resource.parseFile("data/subscription/ok.json").getJsonAsObject<Subscription>()
        subscription.id = scenario.context.subscriptionId
        subscription.orderId = scenario.context.orderId

        givenThat(get(urlEqualTo("/v1/customers/${scenario.context.customerId}/subscriptions/${scenario.context.subscriptionId}"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json; charset=utf-8")
                        .withBody(Json.toJson(subscription))))
    }
}