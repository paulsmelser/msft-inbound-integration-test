package com.appdirect.integration

import com.appdirect.integration.scenario.TestRun
import com.appdirect.integration.scenarios.SubscriptionOrderScenario
import com.appdirect.integration.scenarios.SubscriptionQtyChangeScenario
import com.github.tomakehurst.wiremock.client.WireMock.findAll
import com.github.tomakehurst.wiremock.client.WireMock.patchRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.verify
import com.psmelser.jackson.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SubscriptionChangeTest : IntegrationTest() {
    @Test
    fun `When subscription QTY change is requested, then subscription update with the new QTY must be requested from Microsoft`() {
        TestRun.startWith(SubscriptionOrderScenario(8888))
                .continueWith(SubscriptionQtyChangeScenario(8888, {
                    verify(patchRequestedFor(urlEqualTo("/v1/customers/${it.context.customerId}/subscriptions/${it.context.subscriptionId}")))

                    val matchedRequests = findAll(patchRequestedFor(urlEqualTo("/v1/customers/${it.context.customerId}/subscriptions/${it.context.subscriptionId}")))

                    assertThat(Json.toJsonNode(matchedRequests[0].bodyAsString).path("quantity").asInt()).isEqualTo(it.context.qty)
                }))
                .execute()
    }
}